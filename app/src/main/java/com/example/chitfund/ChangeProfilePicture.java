package com.example.chitfund;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ChangeProfilePicture extends AppCompatActivity implements View.OnClickListener {

    private ShapeableImageView shapeableImageViewProfilePicture;
    private Button closeButton;
    private Button ButtonChangeProfile;
    private final static int galleryRequestCode = 69;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri="";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private StorageReference profileReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_picture);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        shapeableImageViewProfilePicture = findViewById(R.id.shapeableImageViewProfilePicture);
        closeButton = findViewById(R.id.ButtonClose);
        ButtonChangeProfile = findViewById(R.id.buttonChange);



        closeButton.setOnClickListener(this);
        ButtonChangeProfile.setOnClickListener(this);
    }


    private void uploadProfileImage(Uri imageUri){
        StorageReference fileRef = storageReference.child("Users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        Toast.makeText(this, "Uploading! Just wait few seconds", Toast.LENGTH_SHORT).show();
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ChangeProfilePicture.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChangeProfilePicture.this, "Failed to upload", Toast.LENGTH_SHORT).show();
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

    private void changeProfileImage(){
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
                changeProfileImage();
                break;
        }
    }

}