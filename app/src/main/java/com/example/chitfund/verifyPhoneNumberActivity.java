package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class verifyPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextPhoneNumber;
    private Button buttonSubmit;

    private DatabaseReference fDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        editTextPhoneNumber = findViewById(R.id.editTextPhone);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getCurrentUser().getUid());

        buttonSubmit.setOnClickListener(this);
    }

    private void verifyPhoneNumber(){
        String stringPhoneNumber= editTextPhoneNumber.getText().toString().trim();
        if(Patterns.PHONE.matcher(stringPhoneNumber).matches()){
            fDatabase.child("phoneNumber").setValue(stringPhoneNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    startActivity(new Intent(verifyPhoneNumberActivity.this, DashboardActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(verifyPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }
        else{
            editTextPhoneNumber.setError("Please enter valid phone Number");
            editTextPhoneNumber.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case (R.id.buttonSubmit):
                verifyPhoneNumber();
                break;
        }
    }
}