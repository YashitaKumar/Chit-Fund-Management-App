package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    EditText nametxt, upiIdtxt, msgtxt, amttxt;
    String paymentStatus="";

    Button paybtn,paybtn_razor;

    final int PAY_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        nametxt = findViewById(R.id.edtname);
        upiIdtxt = findViewById(R.id.edtupiid);
        msgtxt = findViewById(R.id.edtmsg);
        amttxt = findViewById(R.id.edtamt);


        paybtn = findViewById(R.id.btnpay);
        paybtn_razor=findViewById(R.id.btnpay2);
        paybtn_razor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nametxt.getText().toString();
                String upiId = upiIdtxt.getText().toString();
                String amt = amttxt.getText().toString();
                String msg = msgtxt.getText().toString();


                if(name.isEmpty() || upiId.isEmpty()){
                    Toast.makeText(PaymentActivity.this, "Name and Upi Id is necessary", Toast.LENGTH_SHORT).show();
                }else PayUsingUpi(name,upiId,amt,msg);

            }
        });
    }
    private void PayUsingUpi(String name,String upiId,String amt,String msg){
        Uri uri = new Uri.Builder()
                .scheme("upi").authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",msg)
                .appendQueryParameter("am",amt)
                .appendQueryParameter("mc","BCR2DN6TVOC6LILU")
                .appendQueryParameter("tid","123")
                .appendQueryParameter("tr","1234")
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiIntent = new Intent(Intent.ACTION_VIEW);
        upiIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiIntent,"Pay");
        if(chooser.resolveActivity(getPackageManager()) != null){
            startActivityForResult(chooser,PAY_REQUEST);
        }else{
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAY_REQUEST){

            if(isInternetAvailabe(PaymentActivity.this)){

                if (data == null) {
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    String temp = "nothing";
                    Toast.makeText(this, "Transaction not complete", Toast.LENGTH_SHORT).show();
                    paymentStatus="Transaction Not Complete";
                }else {
                    String text = data.getStringExtra("response");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(text);

                    upiPaymentCheck(text);
                }
                AddInvoiceData(paymentStatus);
            }

        }
    }

    void upiPaymentCheck(String data){
        String str = data;

        String payment_cancel = "";
        String status = "";
        String response[] = str.split("&");

        for (int i = 0; i < response.length; i++)
        {
            String equalStr[] = response[i].split("=");
            if(equalStr.length >= 2)
            {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                {
                    status = equalStr[1].toLowerCase();
                }
            }
            else
            {
                payment_cancel = "Payment cancelled";
            }
        }
        if(status.equals("success")){
            Toast.makeText(this, "Transaction Successfull", Toast.LENGTH_SHORT).show();
            paymentStatus="Transaction Successfull";
        }else if("Payment cancelled".equals(payment_cancel)){
            Toast.makeText(this, "payment cancelled by user", Toast.LENGTH_SHORT).show();
            paymentStatus = "payment cancelled by user";
        }else{
            Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
            paymentStatus = "Transaction Failed";
        }
    }
    public static boolean isInternetAvailabe(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()){
                return true;
            }
        }
        return false;
    }

    private void AddInvoiceData(String paymentStatus)
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String  strDate = formatter.format(date);
        SimpleDateFormat formatterTime= new SimpleDateFormat("h:mm a");
        String strTime = formatterTime.format(date);
        Map<String, Object> map = new HashMap<>();
        map.put("name",nametxt.getText().toString());
        map.put("upiId",upiIdtxt.getText().toString());
        map.put("amount",amttxt.getText().toString());
        map.put("note",msgtxt.getText().toString());
        map.put("status",paymentStatus);
        map.put("date",strDate);
        map.put("time",strTime);

        FirebaseAuth mAuth;
        mAuth= FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"/Invoice").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PaymentActivity.this, "Invoice Generated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, "Error while Invoice Generation", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void startPayment() {

        Checkout checkout = new Checkout();

        checkout.setImage(R.mipmap.ic_launcher);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", R.string.app_name);
            options.put("description", msgtxt.getText().toString());
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);

            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", amttxt.getText().toString()+"00");

            final String[] email = new String[1];
            final String[] contact = new String[1];

            JSONObject preFill = new JSONObject();

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = rootRef.child("Users");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot keyId: snapshot.getChildren()) {
                        email[0] = keyId.child("emailId").getValue(String.class);
                        contact[0] = keyId.child("phoneNumber").getValue(String.class);
                        break;

                    }
                    try {
                        String emailpay,contactpay;
                        emailpay=email[0];
                        contactpay=contact[0];
                        preFill.put("email", emailpay);
                        preFill.put("contact", contactpay);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Failed to read values
                    Log.w("UserRef", "Failed to read User Values ",error.toException() );
                }
            });

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success!"+s, Toast.LENGTH_SHORT).show();
        paymentStatus="Successful";
        AddInvoiceData(paymentStatus);
    }

    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Error!"+s, Toast.LENGTH_SHORT).show();
        paymentStatus="Error";
        AddInvoiceData(paymentStatus);
    }



}