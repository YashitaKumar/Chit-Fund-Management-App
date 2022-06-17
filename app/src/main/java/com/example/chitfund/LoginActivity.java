package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText emailID;
    private EditText password;
    private Button logIn;
    private TextView forgotPaasword;
    private TextView signUP;

    //firebase


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        TextView btnSignUp = findViewById(R.id.btn_SignUp);
        TextView btnForgotPassword = findViewById(R.id.btn_ForgotPassword);
        logIn();
    }

    private void logIn() {
        emailID = findViewById(R.id.input_Email);
        password = findViewById(R.id.editTextTextPassword);
        logIn = findViewById(R.id.btnLogin);
        forgotPaasword = findViewById(R.id.btn_ForgotPassword);
        signUP = findViewById(R.id.btn_SignUp);
        forgotPaasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, VerifyEmailActivity.class));
            }
        });
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EmailId = emailID.getText().toString().trim();
                String pass = password.getText().toString().trim();

//                startActivity(new intent(LoginActivity.this, homepage.class));
            }
        });
    }
}