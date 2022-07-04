package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity{

//    public static final String EXTRA_NAME = "com.example.chitfund.extra.NAME";

    private EditText verifyEmailID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        Button btn = findViewById(R.id.btn_resetPassword);
        verifyEmailID = findViewById(R.id.verify_email);
        mAuth= FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetEmail();
            }
        });
    }
    public void resetEmail(){
        String resetEmailID = verifyEmailID.getText().toString();
//        Intent intent = new Intent(VerifyEmailActivity.this,ResetPasswordActivity.class);
//        intent.putExtra(EXTRA_NAME,nameText);
//        startActivity(intent);
        if(TextUtils.isEmpty(resetEmailID)){
            verifyEmailID.setError("Enter the reset Email Id");
            verifyEmailID.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(resetEmailID).matches()){
            verifyEmailID.setError("Enter valid Email Id");
            verifyEmailID.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(resetEmailID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(VerifyEmailActivity.this, "Please! check your email.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(VerifyEmailActivity.this, "Failed to send the Link for reset of password! Try again.", Toast.LENGTH_SHORT).show();
                    verifyEmailID.requestFocus();
                    verifyEmailID.getText().clear();
                }
            }
        });

    }

}