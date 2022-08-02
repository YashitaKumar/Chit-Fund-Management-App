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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_SIGN_IN = 143;
    public static String phoneNumber = "123";
    private EditText input_emailId;
    private EditText input_password;

    float v=0;

    private Button btn_signIn;
    private TextView btn_signUp;
    private TextView btn_forgotPassword;
    private TextView heading;
    private TextView text;
    private SignInButton signInButton;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference fDatabase;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heading = (TextView) findViewById(R.id.Heading_Register);
        text = (TextView) findViewById(R.id.textView2);

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
        fDatabase = FirebaseDatabase.getInstance().getReference("Users");

        btn_signUp.setOnClickListener(this);
        btn_signIn.setOnClickListener(this);
        btn_forgotPassword.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        //Animation
        input_emailId.setTranslationX(800);
        input_password.setTranslationX(800);
        btn_signIn.setTranslationX(800);
        btn_signUp.setTranslationX(800);
        btn_forgotPassword.setTranslationX(800);
        text.setTranslationX(800);
        signInButton.setTranslationX(800);

        heading.setTranslationY(300);

        heading.setAlpha(v);
        input_emailId.setAlpha(v);
        input_password.setAlpha(v);
        btn_signIn.setAlpha(v);
        btn_signUp.setAlpha(v);
        btn_forgotPassword.setAlpha(v);
        text.setAlpha(v);
        signInButton.setAlpha(v);

        heading.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        input_emailId.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        input_password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        btn_forgotPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        btn_signIn.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        btn_signUp.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(900).start();
        text.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(900).start();
        signInButton.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1100).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()){
            startActivity(new Intent(this,DashboardActivity.class));
            finish();
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
                        return;
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    progressBar.setVisibility(View.GONE);

                    switch (errorCode) {

                        case "ERROR_INVALID_CUSTOM_TOKEN":
                            Toast.makeText(MainActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(MainActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(MainActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(MainActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                            input_emailId.setError("The email address is badly formatted.");
                            input_emailId.requestFocus();
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(MainActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_REQUIRES_RECENT_LOGIN":
                            Toast.makeText(MainActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(MainActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(MainActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                            input_emailId.setError("The email address is already in use by another account.");
                            input_emailId.requestFocus();
                            break;

                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(MainActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(MainActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_TOKEN_EXPIRED":
                            Toast.makeText(MainActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(MainActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_USER_TOKEN":
                            Toast.makeText(MainActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_OPERATION_NOT_ALLOWED":
                            Toast.makeText(MainActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                            break;

                        default:
                            Toast.makeText(MainActivity.this, errorCode, Toast.LENGTH_SHORT).show();


                    }
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
                               DatabaseReference userPhoneNumberRef=fDatabase.child(mAuth.getCurrentUser().getUid());
                               Log.d("SignInActivity", userPhoneNumberRef.getKey());
//                               userPhoneNumberRef.addValueEventListener(new ValueEventListener() {
//                                   @Override
//                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                       phoneNumber = snapshot.getValue(String.class);
//                                       Log.d("PhoneNumberRetrieval",phoneNumber);
//                                   }
//                                   @Override
//                                   public void onCancelled(@NonNull DatabaseError error) {
//                                       Log.w("PhoneNumberRetrieval", "onCancelled: h" );
//                                   }
//                               });
                            userPhoneNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot data: snapshot.getChildren()){
//                                        Boolean truth = data.child("phoneNumber").exists();
                                        if (data.getKey().equals("phoneNumber")) {
                                            phoneNumber = data.getValue(String.class);
                                            if(!phoneNumber.equals("123")){
                                                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                                return;
                                            }
//                                            Log.d("PhoneNumberThing", "Phone doesnot exits yet"+ (new Boolean(data.child("phoneNumber").exists()).toString()) +data.getValue(String.class));
                                        }
//                                        else {
//                                            Log.w("PhoneNumberThing", "Phone doesnot exits yet"+ (new Boolean(data.child("phoneNumber").exists()).toString()) +data.getValue(String.class));
//                                        }
                                    }
                                    Log.d("PhoneNumberThing",phoneNumber);
                                    FirebaseUser fUser = mAuth.getCurrentUser();
                                    String fullName = fUser.getDisplayName();
                                    String[] parts = fullName.split("\\s+");
                                    User user = new User(parts[0], parts[1], phoneNumber, fUser.getEmail());
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("SignInActivity", "signInWithCredential:success");
                                            startActivity(new Intent(MainActivity.this, verifyPhoneNumberActivity.class));
                                            return;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.w("SignInActivity", "SignUpWithGoogle:failure");
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w("PhoneNumberThing", "Something went wrong!");
                                }
                            });
                            Log.d("PhoneNumberThing",phoneNumber);
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