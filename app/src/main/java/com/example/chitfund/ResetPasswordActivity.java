package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Button btn = findViewById(R.id.btn_Confirm);
//        Intent intent=getIntent();
//        String name = intent.getStringExtra(VerifyEmailActivity.EXTRA_NAME);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
            }
        });
//        FirebaseAuth.getInstance().;
    }
}