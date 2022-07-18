package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText fname,lname,email,mobile,slab,image;
    Button btnAdd,btnBack;
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
    }
}