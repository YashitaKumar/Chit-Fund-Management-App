package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView heading;
    private TextView text;
    private EditText Password;
    private EditText RePassword;
    private Button BtnConfirm;

    float v=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        heading = findViewById(R.id.Heading_Register);
        text = findViewById(R.id.textView3);
        Password = findViewById(R.id.editTextPassword3);
        RePassword = findViewById(R.id.editTextPassword2);
        BtnConfirm = findViewById(R.id.btn_Confirm);

        //Animation
        heading.setTranslationY(300);
        text.setTranslationY(300);
        Password.setTranslationX(800);
        RePassword.setTranslationX(800);
        BtnConfirm.setTranslationX(800);

        heading.setAlpha(v);
        text.setAlpha(v);
        Password.setAlpha(v);
        RePassword.setAlpha(v);
        BtnConfirm.setAlpha(v);

        heading.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        text.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        Password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        RePassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        BtnConfirm.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();

    }
}