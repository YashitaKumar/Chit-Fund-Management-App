package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth;

=======
>>>>>>> e5fc174a323c6ab2356b649aff96329c493f6fa3
public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Button btn = findViewById(R.id.btn_Confirm);
<<<<<<< HEAD
//        Intent intent=getIntent();
//        String name = intent.getStringExtra(VerifyEmailActivity.EXTRA_NAME);
=======
>>>>>>> e5fc174a323c6ab2356b649aff96329c493f6fa3
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
            }
        });
<<<<<<< HEAD
//        FirebaseAuth.getInstance().;
=======
>>>>>>> e5fc174a323c6ab2356b649aff96329c493f6fa3
    }
}