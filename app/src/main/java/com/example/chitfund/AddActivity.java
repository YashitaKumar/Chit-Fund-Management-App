package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddActivity extends AppCompatActivity {

    EditText fname,lname,email,mobile,slab,image,uid;
    Button btnAdd,btnBack;
    private String  strDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        fname=findViewById(R.id.txtfname);
        lname=findViewById(R.id.txtlname);
        email=findViewById(R.id.txtemail);
        mobile=findViewById(R.id.txtmobile);
        slab=findViewById(R.id.txtslab);
        image=findViewById(R.id.txtimage);
        uid = findViewById(R.id.txtuid);

        btnAdd=findViewById(R.id.btnAdd);
        btnBack=findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void insertData()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("fname",fname.getText().toString());
        map.put("lname",lname.getText().toString());
        map.put("email",email.getText().toString());
        map.put("mobile",mobile.getText().toString());
        map.put("slab",slab.getText().toString());
        map.put("image",image.getText().toString());
        map.put("uid",uid.getText().toString().trim().substring(0,15));

        FirebaseAuth mAuth;
        mAuth= FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"/Customers").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddActivity.this, "Error while Insertion", Toast.LENGTH_SHORT).show();
                    }
                });

        //Generating Chits in other members application
        float amount;
        int slabAmt;
        slabAmt = Integer.parseInt(slab.getText().toString());
        amount = slabAmt/20;


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 2);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        strDate = formatter.format(date);
        Date future = cal.getTime();
        String strFuture = formatter.format(future);



        Map<String, Object> chitMap = new HashMap<>();
        chitMap.put("chitid",mAuth.getCurrentUser().getUid().substring(5,10));
        chitMap.put("slab",slab.getText().toString());
        chitMap.put("monthly",Float.toString(amount));
        chitMap.put("timeline","20 Months");
        chitMap.put("Startdate",strDate);
        chitMap.put("Duedate",strFuture);

        FirebaseDatabase.getInstance().getReference().child("Users/"+uid.getText().toString()+"/Chits").push()
                .setValue(chitMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddActivity.this, "Record Created", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddActivity.this, "Error while Insertion", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}