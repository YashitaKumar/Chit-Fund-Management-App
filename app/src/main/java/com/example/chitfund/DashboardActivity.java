package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView LogoutButton;
    private CardView ChangeProfileImage;
    private CardView CustomerButton;
    private CardView PaymentButton;
    private CardView InvoiceButton;
    private CardView MathButton;
    private CardView ChitsButton;

    private TextView PhoneNumber;
    private TextView EmailId;
    private TextView UserName;
    private TextView UserId;

    private Button btn_more;
    private Button btn_back;


    private ShapeableImageView shapeableImageViewProfileImage;

    public static String fName,lName,phoneNo,emailId;

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private StorageReference profileReference;


    private String userID;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        ChangeProfileImage = findViewById(R.id.cardCustomer);
        shapeableImageViewProfileImage = findViewById(R.id.shapeableImageViewProfileImage);


        PhoneNumber = findViewById(R.id.TextViewUserPhoneNumber);
        EmailId = findViewById(R.id.TextViewUserEmailId);
        UserName = findViewById(R.id.TextViewUserName);
        UserId = findViewById(R.id.TextViewUserId);

        LogoutButton = findViewById(R.id.Logout);
        CustomerButton = findViewById(R.id.Customers);
        PaymentButton = findViewById(R.id.payments);
        InvoiceButton = findViewById(R.id.Invoices);
        MathButton = findViewById(R.id.Maths);
        ChitsButton = findViewById(R.id.Chits);

        btn_more = findViewById(R.id.btn_more);


        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        profileReference = storageReference.child("Users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(shapeableImageViewProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashboardActivity.this, "Failed to load the profile picture!", Toast.LENGTH_SHORT).show();
            }
        });


        emailId= mAuth.getCurrentUser().getEmail().toString().trim();
        EmailId.setText(emailId);
        UserId.setText("UID: "+ mAuth.getCurrentUser().getUid().toString().trim().substring(0,15));

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Users");
        Log.v("USERID",userRef.getKey());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId: snapshot.getChildren()) {
                    if (keyId.child("emailId").getValue().equals(emailId)) {
                        fName = keyId.child("fName").getValue(String.class);
                        lName = keyId.child("lName").getValue(String.class);
                        phoneNo = keyId.child("phoneNumber").getValue(String.class);
                        break;
                    }
                }
                UserName.setText(fName+" "+lName);
                PhoneNumber.setText(phoneNo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Failed to read values
                Log.w("UserRef", "Failed to read User Values ",error.toException() );
            }
        });

        LogoutButton.setOnClickListener(this);
        ChangeProfileImage.setOnClickListener(this);
        CustomerButton.setOnClickListener(this);
        PaymentButton.setOnClickListener(this);
        InvoiceButton.setOnClickListener(this);
        MathButton.setOnClickListener(this);
        ChitsButton.setOnClickListener(this);

        //Dashboard Know More

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
        final View rules_popup = getLayoutInflater().inflate(R.layout.rules,null);
        btn_back = rules_popup.findViewById(R.id.backbtn);
        dialogBuilder.setView(rules_popup);
        AlertDialog dialog = dialogBuilder.create();

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void UserLogOut() {
        mAuth.signOut();
        startActivity(new Intent(DashboardActivity.this,MainActivity.class));
        finish();
    }
    private void changeProfile(){
        startActivity(new Intent(this, ChangeProfile.class));
    }
    private void crudCustomers(){
        startActivity(new Intent(this, CustomerActivity.class));
    }
    private void payment(){
        startActivity(new Intent(this, PaymentActivity.class));
    }
    private void invoices(){
        startActivity(new Intent(this, InvoiceActivity.class));
    }
    private void maths() {
        startActivity(new Intent(this, MathActivity.class));
    }
    private void chits() {
        startActivity(new Intent(this, ChitsActivity.class));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.Logout):
                UserLogOut();
                break;
            case (R.id.cardCustomer):
                changeProfile();
                break;
            case (R.id.Customers):
                crudCustomers();
                break;
            case (R.id.payments):
                payment();
                break;
            case (R.id.Invoices):
                invoices();
                break;
            case (R.id.Maths):
                maths();
                break;
            case (R.id.Chits):
                chits();
                break;
        }
    }


}