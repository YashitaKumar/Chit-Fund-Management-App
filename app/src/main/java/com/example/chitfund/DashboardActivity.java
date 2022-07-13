package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView LogoutButton;
    private CardView ChangeProfile;
    private CardView paymentPage;
    private TextView PhoneNumber;
    private TextView EmailId;
    private TextView UserName;
    private TextView UserId;


    private ShapeableImageView shapeableImageViewProfileImage;

    public static String fName,lName,phoneNo,emailId;

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private StorageReference profileReference;


    private String userID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ChangeProfile = findViewById(R.id.ImageViewUserProfilePic);
        shapeableImageViewProfileImage = findViewById(R.id.shapeableImageViewProfileImage);
        paymentPage = findViewById(R.id.payments);

        PhoneNumber = findViewById(R.id.TextViewUserPhoneNumber);
        EmailId = findViewById(R.id.TextViewUserEmailId);
        UserName = findViewById(R.id.TextViewUserName);
        UserId = findViewById(R.id.TextViewUserId);

        LogoutButton = findViewById(R.id.Logout);

        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        profileReference = storageReference.child("Users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(shapeableImageViewProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashboardActivity.this, "Failed to load the profile picture!", Toast.LENGTH_SHORT).show();
            }
        });


        emailId= mAuth.getCurrentUser().getEmail().toString().trim();
        EmailId.setText("Email ID: "+ emailId);
        UserId.setText("UID: "+ mAuth.getCurrentUser().getUid().toString().trim());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Users");
        Log.v("USERID",userRef.getKey());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId: snapshot.getChildren()) {
//                    if(fName==null || phoneNo==null){
//                        fName = mAuth.getCurrentUser().getDisplayName();
//                        String parts[]= fName.split("\\s+");
//                        fName = parts[0];
//                        lName = parts[1];
//                        phoneNo = mAuth.getCurrentUser().getPhoneNumber();
//                    }
                    if (keyId.child("emailId").getValue().equals(emailId)) {
                        fName = keyId.child("fName").getValue(String.class);
                        lName = keyId.child("lName").getValue(String.class);
                        phoneNo = keyId.child("phoneNumber").getValue(String.class);
                        break;
                    }
                }

                UserName.setText(fName+" "+lName);
                PhoneNumber.setText("Phone No.: "+phoneNo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Failed to read values
                Log.w("UserRef", "Failed to read User Values ",error.toException() );
            }
        });

        LogoutButton.setOnClickListener(this);
        ChangeProfile.setOnClickListener(this);
        paymentPage.setOnClickListener(this);
    }

    private void UserLogOut() {
        mAuth.signOut();
        finish();
    }
    private void changeProfile(){
       startActivity(new Intent(this, ChangeProfile.class));
    }

    private void paymentPage(){ startActivity(new Intent(this, BillPayNowActivity.class));}
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.Logout):
                UserLogOut();
                break;
            case (R.id.ImageViewUserProfilePic):
                changeProfile();
                break;
            case(R.id.payments):
                paymentPage();
                break;
        }
    }

}