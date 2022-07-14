package com.example.chitfund;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ChangeProfile extends AppCompatActivity implements View.OnClickListener {

    private ShapeableImageView shapeableImageViewProfilePicture;
    private Button closeButton;
    private Button ButtonChangeProfile;
    private EditText fName,lName,phoneNumber;

    private final static int galleryRequestCode = 69;

    private FirebaseAuth mAuth;
    private DatabaseReference fDatabase;

    private StorageReference storageReference;
    private StorageReference profileReference;

    //on creating the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_picture);

        //Defining the variables
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fDatabase = FirebaseDatabase.getInstance().getReference("Users");

        shapeableImageViewProfilePicture = findViewById(R.id.shapeableImageViewProfilePicture);
        closeButton = findViewById(R.id.ButtonClose);
        ButtonChangeProfile = findViewById(R.id.buttonChange);
        fName = findViewById(R.id.editTextFName);
        lName = findViewById(R.id.editTextLName);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        //downloading profile.jpg from firebase storage
        profileReference = storageReference.child("Users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(shapeableImageViewProfilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeProfile.this, "Failed to load the profile picture!", Toast.LENGTH_SHORT).show();
            }
        });

        fName.setText(DashboardActivity.fName, TextView.BufferType.EDITABLE);
        lName.setText(DashboardActivity.lName, TextView.BufferType.EDITABLE);
        phoneNumber.setText(DashboardActivity.phoneNo, TextView.BufferType.EDITABLE);

        closeButton.setOnClickListener(this);
        ButtonChangeProfile.setOnClickListener(this);
        shapeableImageViewProfilePicture.setOnClickListener(this);
    }

    private void changeProfile(){
        if(fName.getText().toString().trim().isEmpty()||
                lName.getText().toString().trim().isEmpty()||
                phoneNumber.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
        }
        HashMap<String,Object> user = new HashMap();

        user.put("fName",fName.getText().toString().trim());
        user.put("lName",lName.getText().toString().trim());
        user.put("phoneNumber",phoneNumber.getText().toString().trim());

        fDatabase.child(mAuth.getCurrentUser().getUid()).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(ChangeProfile.this, DashboardActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryRequestCode && resultCode== Activity.RESULT_OK){
            Uri imageUri= data.getData();
//            shapeableImageViewProfilePicture.setImageURI(imageUri);
            uploadProfileImage(imageUri);
        }
    }

    private void uploadProfileImage(Uri imageUri){
        StorageReference fileRef = storageReference.child("Users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        Toast.makeText(this, "Uploading! Just wait few seconds", Toast.LENGTH_SHORT).show();
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ChangeProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(shapeableImageViewProfilePicture);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeProfile.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changingProfilePicture(){
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent,galleryRequestCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.ButtonClose):
                finish();
                break;
            case (R.id.buttonChange):
                changeProfile();
                break;
            case (R.id.shapeableImageViewProfilePicture):
                changingProfilePicture();
                break;
        }
    }
}