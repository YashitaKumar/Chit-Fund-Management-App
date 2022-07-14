package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_SIGN_IN = 143;
    public static String phoneNumber = "123";
    private EditText input_emailId;
    private EditText input_password;

    private Button btn_signIn;
    private TextView btn_signUp;
    private TextView btn_forgotPassword;
    private SignInButton signInButton;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_emailId=(EditText) findViewById(R.id.login_email);
        input_password=(EditText) findViewById(R.id.inputPasswordLogin);

        btn_signUp=(TextView) findViewById(R.id.btn_SignUp);
        btn_signIn=(Button) findViewById(R.id.btnLogin);
        btn_forgotPassword=(TextView) findViewById(R.id.btn_ForgotPassword);
        signInButton = findViewById(R.id.SignInGoogleButton);

        progressBar=(ProgressBar) findViewById(R.id.progressBarSignIn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        btn_signUp.setOnClickListener(this);
        btn_signIn.setOnClickListener(this);
        btn_forgotPassword.setOnClickListener(this);
        signInButton.setOnClickListener(this);
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
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch(ApiException e){
                Toast.makeText(this, e.toString().trim(), Toast.LENGTH_SHORT).show();
                Log.w("SignInActivity", "signInWithCredential:failure", task.getException());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            String fullName = fUser.getDisplayName();
                            String[] parts = fullName.split("\\s+");
                            User user = new User(parts[0], parts[1],phoneNumber,fUser.getEmail());
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("SignInActivity", "signInWithCredential:success");
                                    startActivity(new Intent(MainActivity.this,verifyPhoneNumberActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.w("SignInActivity", "SignUpWithGoogle:failure");
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInActivity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
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
           case (R.id.SignInGoogleButton):
               signInGoogle();
               break;

       }
    }
}