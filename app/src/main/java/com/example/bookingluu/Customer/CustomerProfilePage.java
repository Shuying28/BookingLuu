package com.example.bookingluu.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.CustomerLoginPage;
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
    private ImageView profilePic, editImageBtn, myProfileBackBtn, editDetailBtn;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    String userId;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    ProgressDialog progressDialog;
    private Dialog logoutDialog, editProfileDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_page);
        init();

        myProfileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), RestaurantListPage.class));
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerProfilePage.this, ReservationHistoryPage.class));
            }
        });

        documentReference= fStore.collection("customers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fNameText.setText(value.getString("fullName"));
                eText.setText(value.getString("email"));
                pNumberText.setText(value.getString("phoneNumber"));
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


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog = new Dialog(CustomerProfilePage.this);
                logoutDialog.setContentView(R.layout.dialog_customer_logout);
                Button yesBtn= logoutDialog.findViewById(R.id.deleteMenuBtn);
                Button noBtn= logoutDialog.findViewById(R.id.cancelDeleteMenuBtn);
                logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                logoutDialog.show();

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        logoutDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));
                        finish();
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                    }
                });


            }
        });


         //Todo: editProfileDialog and editProfile in database : all member can try
        // 1.用和我pop up logout dialog 的方法pop edit profile dialog
        // 2.去看我怎样update image 的field  在line 199 - 用来update 名字和电话号码
        //Shuying
        editDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileDialog = new Dialog(CustomerProfilePage.this);
                editProfileDialog.setContentView(R.layout.dialog_edit_profile);
                Button saveBtn= editProfileDialog.findViewById(R.id.saveBtn);
                Button cancelBtn= editProfileDialog.findViewById(R.id.cancelBtn);
                editProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editProfileDialog.show();

                EditText phoneNoText= editProfileDialog.findViewById(R.id.phoneNoText);
                EditText customerNameText= editProfileDialog.findViewById(R.id.customerNameText);

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //change ;(
                        String custName = customerNameText.getText().toString();
                        String custPhoneNo = phoneNoText.getText().toString();
                        updateDetailsToFirebase(custName,custPhoneNo);

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editProfileDialog.dismiss();
                    }
                });


            }
        });


    }

    public void init(){
        fNameText=findViewById(R.id.fNameText);
        pNumberText=findViewById(R.id.pNumberText);
        eText=findViewById(R.id.eText);
        homeBtn=findViewById(R.id.homeBtn);
        historyBtn=findViewById(R.id.historyBtn);
        logoutBtn=findViewById(R.id.logoutBtn);
        profilePic=findViewById(R.id.profilePic);
        myProfileBackBtn=findViewById(R.id.myProfileBackBtn);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        userId=fAuth.getCurrentUser().getUid();
        editImageBtn=findViewById(R.id.editImageBtn);
        progressDialog =new ProgressDialog(this);
        editDetailBtn= findViewById(R.id.editDetailBtn);


    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), RestaurantListPage.class));
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
                       documentReference.update("image",uri);
                   }
               });
                Toast.makeText(CustomerProfilePage.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerProfilePage.this, "Upload Fail, Please Try Again ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDetailsToFirebase(String custName, String custPhoneNo) {
        //Update details to firebase
        documentReference= fStore.collection("customers").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                System.out.println("ugiyf97td7fouygyu"+custName);

                if (!TextUtils.isEmpty(custName)&&!TextUtils.isEmpty(custPhoneNo)){
                    fNameText.setText(custName);
                    pNumberText.setText(custPhoneNo);
                    documentReference.update("fullName", custName);
                    documentReference.update("phoneNumber", custPhoneNo);

                }else if(!TextUtils.isEmpty(custName)){
                    fNameText.setText(custName);
                    documentReference.update("fullName", custName);

                }else if(!TextUtils.isEmpty(custPhoneNo)){
                    pNumberText.setText(custPhoneNo);
                    documentReference.update("phoneNumber", custPhoneNo);

                }else{
                    Toast.makeText(CustomerProfilePage.this, "Profile Not Updated", Toast.LENGTH_SHORT).show();
                    editProfileDialog.dismiss();
                    return;

                }
                editProfileDialog.dismiss();
                Toast.makeText(CustomerProfilePage.this, "Profile Updated", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerProfilePage.this, "Update Fail, Please Try Again ", Toast.LENGTH_SHORT).show();
            }
        });


    }

}