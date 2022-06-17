package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button registrationButton;
    private EditText firstName;
    private EditText lastName;
    private EditText emailID;
    private EditText PhoneNo;
    private EditText Password;
    private TextView signUp;
    private EditText rePassword;

    private ProgressDialog progDialog;

    //firebase:
    private FirebaseAuth Authen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Authen = FirebaseAuth.getInstance();
        progDialog = new ProgressDialog(this);

        registration();
    }
    private void registration(){
        registrationButton = findViewById(R.id.btnRegister);
        firstName = findViewById(R.id.input_firstName);
        lastName = findViewById(R.id.input_lastName);
        emailID = findViewById(R.id.input_Email);
        PhoneNo = findViewById(R.id.input_phoneNo);
        Password = findViewById(R.id.input_password);
        rePassword = findViewById(R.id.input_password2);
        signUp = findViewById(R.id.btn_SignIn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName= firstName.getText().toString().trim();
                String lName= lastName.getText().toString().trim();
                String EmailId = emailID.getText().toString().trim();
                String phoneNo = PhoneNo.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String rePass = rePassword.getText().toString().trim();
                if(TextUtils.isEmpty(fName)){
                    firstName.setError("please fill first name");
                    return;
                }
                if(TextUtils.isEmpty(lName)){
                    lastName.setError("please fill last name");
                    return;
                }
                if(TextUtils.isEmpty(EmailId)){
                    emailID.setError("please fill email id");
                    return;
                }
                if(TextUtils.isEmpty(phoneNo)){
                    PhoneNo.setError("please fill phone number");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Password.setError("please enter the password");
                    return;
                }
                if(TextUtils.isEmpty(rePass)){
                    rePassword.setError("please reenter the password");
                    return;
                }
                if(!password.equals(rePass)){
                    rePassword.setError("passwords are not matching!");
                }

                progDialog.setMessage("Processing..");

                Authen.createUserWithEmailAndPassword(EmailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            progDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Registration Complete!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }
                        else{
                            progDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Registation Faild!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}