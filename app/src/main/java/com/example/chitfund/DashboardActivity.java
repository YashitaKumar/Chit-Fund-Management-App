package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView LogoutButton;
    private TextView PhoneNumber;
    private TextView EmailId;
    private TextView UserName;
    private TextView UserId;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    private String userID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        PhoneNumber = findViewById(R.id.TextViewUserPhoneNumber);
        EmailId = findViewById(R.id.TextViewUserEmailId);
        UserName = findViewById(R.id.TextViewUserName);
        UserId = findViewById(R.id.TextViewUserId);

        LogoutButton = findViewById(R.id.Logout);

        mAuth = FirebaseAuth.getInstance();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        userRef = FirebaseDatabase.getInstance().getReference();
//        userID = user.getUid();
//
//        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User userProfile = snapshot.getValue(User.class);
//                if(userProfile != null){
//                    String fName = userProfile.getfName();
//                    String lName = userProfile.getlName();
//                    String phoneNumber = userProfile.getPhoneNumber();
//                    String emailId = userProfile.getEmailId();
//
//                    PhoneNumber.setText("Phone No.: "+phoneNumber);
//                    UserName.setText(fName+lName);
//                    EmailId.setText("Email ID: "+ emailId);
//                    UserId.setText("UID: "+userID);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(DashboardActivity.this, "Something's Wrong!", Toast.LENGTH_SHORT).show();
//            }
//        });
//

        String emailId= mAuth.getCurrentUser().getEmail().toString().trim();
        EmailId.setText("Email ID: "+ emailId);
        UserId.setText("UID: "+ mAuth.getCurrentUser().getUid().toString().trim());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Users");
        Log.v("USERID",userRef.getKey());

        userRef.addValueEventListener(new ValueEventListener() {
            String fName,lName,phoneNo,emailID;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId: snapshot.getChildren()) {
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
    }

    private void UserLogOut() {
        mAuth.signOut();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.Logout):
                UserLogOut();
                break;
            case (R.id.CRUD):

                break;
        }
    }

}