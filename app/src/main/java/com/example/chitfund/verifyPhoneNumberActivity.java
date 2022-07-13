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
import com.google.firebase.database.FirebaseDatabase;

public class verifyPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextPhoneNumber;
    private Button buttonSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        editTextPhoneNumber = findViewById(R.id.editTextPhone);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(this);
    }

    private void verifyPhoneNumber(){
        String stringPhoneNumber= editTextPhoneNumber.getText().toString().trim();
        if(Patterns.PHONE.matcher(stringPhoneNumber).matches()){
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phoneNumber").setValue(stringPhoneNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("PhoneNumberVerification","PhoneNumberVerification:Success");
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(verifyPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.w("PhoneNumberVerification", "PhoneNumberVerification:Failure");
                }
            });
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