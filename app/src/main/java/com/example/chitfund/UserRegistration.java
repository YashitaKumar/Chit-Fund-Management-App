package com.example.chitfund;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener{

    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPhoneNumber;
    private EditText inputEmailID;
    private EditText inputPassword;
    private EditText inputRePassword;
    private Button buttonRegister;
    private TextView buttonSignIn;
    private TextView textView;
    private TextView heading;
    private ProgressBar progressBar;

    float v =0;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        inputFirstName=(EditText) findViewById(R.id.input_firstName);
        inputLastName=(EditText) findViewById(R.id.input_lastName);
        inputPhoneNumber=(EditText) findViewById(R.id.input_phoneNo);
        inputEmailID=(EditText) findViewById(R.id.input_emailId);
        inputPassword=(EditText) findViewById(R.id.input_password);
        inputRePassword=(EditText) findViewById(R.id.input_rePassword);
        heading = (TextView) findViewById(R.id.Heading_Register);
        textView = (TextView) findViewById(R.id.plainText);

        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister=(Button) findViewById(R.id.btn_Register);
        buttonSignIn=(TextView) findViewById(R.id.btn_SignIn);

        buttonRegister.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        //Animation
        heading.setTranslationY(300);

        inputFirstName.setTranslationX(800);
        inputLastName.setTranslationX(800);
        inputEmailID.setTranslationX(800);
        inputPhoneNumber.setTranslationX(800);
        inputPassword.setTranslationX(800);
        inputRePassword.setTranslationX(800);
        buttonRegister.setTranslationX(800);
        textView.setTranslationX(800);
        buttonSignIn.setTranslationX(800);

        heading.setAlpha(v);
        inputFirstName.setAlpha(v);
        inputLastName.setAlpha(v);
        inputEmailID.setAlpha(v);
        inputPhoneNumber.setAlpha(v);
        inputPassword.setAlpha(v);
        inputRePassword.setAlpha(v);
        textView.setAlpha(v);
        buttonSignIn.setAlpha(v);
        buttonRegister.setAlpha(v);

        heading.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        inputFirstName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        inputLastName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        inputEmailID.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        inputPhoneNumber.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(900).start();
        inputPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1100).start();
        inputRePassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1300).start();
        buttonRegister.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1500).start();
        textView.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1700).start();
        buttonSignIn.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1700).start();


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }

    public void registration(){
        String fName=inputFirstName.getText().toString().trim();
        String lName=inputLastName.getText().toString().trim();
        String phoneNumber=inputPhoneNumber.getText().toString().trim();
        String emailId=inputEmailID.getText().toString().trim();
        String password=inputPassword.getText().toString().trim();
        String rePassword=inputRePassword.getText().toString().trim();


        if(TextUtils.isEmpty(fName)){
            inputFirstName.setError("Enter First Name");
            inputFirstName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(lName)){
            inputLastName.setError("Enter Last Name");
            inputLastName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            inputPhoneNumber.setError("Enter Phone Number");
            inputPhoneNumber.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(phoneNumber).matches()){
            inputPhoneNumber.setError("Enter valid phone number");
            inputPhoneNumber.requestFocus();
        }
        if(TextUtils.isEmpty(emailId)){
            inputEmailID.setError("Enter Email Id");
            inputEmailID.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            inputEmailID.setError("Enter valid Email Id");
            inputEmailID.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            inputPassword.setError("Enter password");
            inputPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            inputPassword.setError("Minimum length of the password should be 6");
            inputPassword.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(rePassword)){
            inputRePassword.setError("Enter password again");
            inputRePassword.requestFocus();
            return;
        }
        if(!password.equals(rePassword)){
            inputRePassword.setError("Enter same password again");
            inputRePassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    User user=new User(fName,lName,phoneNumber,emailId);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UserRegistration.this, "User is registered successfully", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                            else{
                                Toast.makeText(UserRegistration.this, "User is not register,Try again!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    progressBar.setVisibility(View.GONE);

                    switch (errorCode) {

                        case "ERROR_INVALID_CUSTOM_TOKEN":
                            Toast.makeText(UserRegistration.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(UserRegistration.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(UserRegistration.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(UserRegistration.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                            inputEmailID.setError("The email address is badly formatted.");
                            inputEmailID.requestFocus();
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(UserRegistration.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_REQUIRES_RECENT_LOGIN":
                            Toast.makeText(UserRegistration.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(UserRegistration.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(UserRegistration.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                            inputEmailID.setError("The email address is already in use by another account.");
                            inputEmailID.requestFocus();
                            break;

                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(UserRegistration.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(UserRegistration.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_TOKEN_EXPIRED":
                            Toast.makeText(UserRegistration.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(UserRegistration.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_USER_TOKEN":
                            Toast.makeText(UserRegistration.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_OPERATION_NOT_ALLOWED":
                            Toast.makeText(UserRegistration.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                            break;

                        default:
                            Toast.makeText(UserRegistration.this, errorCode, Toast.LENGTH_SHORT).show();
                        

                    }
                }

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case (R.id.btn_Register):
                registration();
                break;
            case (R.id.btn_SignIn):
                finish();
                break;
        }
    }

}