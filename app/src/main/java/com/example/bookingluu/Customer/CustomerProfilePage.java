package com.example.bookingluu.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CustomerProfilePage extends AppCompatActivity {
    private TextView fNameText, pNumberText,eText;
    private Button homeBtn, historyBtn, logoutBtn;
    private ImageView profilePic, editImageBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    DocumentReference documentReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_page);
        init();

        documentReference= fStore.collection("customers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fNameText.setText(value.getString("Full Name"));
                eText.setText(value.getString("Email"));
                pNumberText.setText(value.getString("Phone Number"));
            }
        });

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        StorageReference profileRef =storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });


    }

    public void init(){
        fNameText=findViewById(R.id.fNameText);
        pNumberText=findViewById(R.id.pNumberText);
        eText=findViewById(R.id.eText);
        homeBtn=findViewById(R.id.homeBtn);
        historyBtn=findViewById(R.id.historyBtn);
        logoutBtn=findViewById(R.id.loginBtn);
        profilePic=findViewById(R.id.profilePic);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        userId=fAuth.getCurrentUser().getUid();
        editImageBtn=findViewById(R.id.editImageBtn);
        progressDialog =new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&&resultCode== Activity.RESULT_OK){

            try{
                Uri imageUri =data.getData();
                uploadImageToFirebase(imageUri);
                progressDialog.setMessage("Updating Profile Picture");
                progressDialog.show();

            }catch(NullPointerException ex){

            }




        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //Upload image to firebase
        StorageReference fileRef= storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       Picasso.get().load(uri).into(profilePic);

                   }
               });
                Toast.makeText(CustomerProfilePage.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                documentReference.update("image","users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerProfilePage.this, "Upload Fail, Please Try Again ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}