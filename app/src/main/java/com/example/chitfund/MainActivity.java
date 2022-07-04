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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText input_emailId;
    private EditText input_password;

    private Button btn_signIn;
    private TextView btn_signUp;
    private TextView btn_forgotPassword;

    private ImageView btn_imageViewGoogle;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        oneTapClient = Identity.getSignInClient(this);
//        signInRequest = BeginSignInRequest.builder()
//                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
//                        .setSupported(true)
//                        .build())
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                // Automatically sign in when exactly one credential is retrieved.
//                .setAutoSelectEnabled(true)
//                .build();


        input_emailId=(EditText) findViewById(R.id.login_email);
        input_password=(EditText) findViewById(R.id.inputPasswordLogin);

        btn_signUp=(TextView) findViewById(R.id.btn_SignUp);
        btn_signIn=(Button) findViewById(R.id.btnLogin);
        btn_forgotPassword=(TextView) findViewById(R.id.btn_ForgotPassword);
//        btn_imageViewGoogle=(ImageView) findViewById(R.id.imageViewGoogleIcon);

        progressBar=(ProgressBar) findViewById(R.id.progressBarSignIn);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        btn_signUp.setOnClickListener(this);
        btn_signIn.setOnClickListener(this);
        btn_forgotPassword.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()){
            startActivity(new Intent(this,DashboardActivity.class));
        }
    }

    public void SignIn(){
        String emailId=input_emailId.getText().toString().trim();
        String password=input_password.getText().toString().trim();

        if(TextUtils.isEmpty(emailId)){
            input_emailId.setError("Enter Email Id");
            input_emailId.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            input_emailId.setError("Enter valid Email Id");
            input_emailId.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            input_password.setError("Enter password");
            input_password.requestFocus();
            return;
        }
        if(password.length()<6){
            input_password.setError("Minimum length of the password should be 6");
            input_password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailId,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Failed to Login! Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signInGoogle(){

    }

    @Override
    public void onClick(View view) {
       switch(view.getId()){
           case (R.id.btn_ForgotPassword):
               startActivity(new Intent(this, VerifyEmailActivity.class));
               break;
           case (R.id.btn_SignUp):
               startActivity(new Intent(this,UserRegistration.class));
               break;
           case (R.id.btnLogin):
               SignIn();
               break;

       }
    }
}