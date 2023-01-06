package com.example.bookingluu.Admin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Menu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

public class AdminMainPage extends AppCompatActivity {

    AdminViewPagerAdapter viewPagerFragmentAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    private Button backBtn;
    private Dialog logoutDialog;
    private String[] titles= new String[]{"PENDING","UPCOMING","MENU"};
    private FloatingActionButton addMenuBtn;
    private Dialog addMenuDialog;
    StorageReference storageReference;
    DocumentReference documentReference;
    FirebaseFirestore fStore;
    String menuCode;
    ShapeableImageView menuImage;
    String imageLink;
    private final String RESTAURANT_OF_ADMIN= AdminLoginPage.restaurantOfAdmin;
    String restaurantImage, restaurantLogo;
    private ImageView adminRestaurant, adminLogo;
    private TextView restaurantName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        init();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog = new Dialog(AdminMainPage.this);
                logoutDialog.setContentView(R.layout.dialog_admin_logout);
                Button yesBtn= logoutDialog.findViewById(R.id.deleteMenuBtn);
                Button noBtn= logoutDialog.findViewById(R.id.cancelDeleteMenuBtn);
                logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                logoutDialog.show();
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        logoutDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), AdminLoginPage.class));
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

        viewPagerFragmentAdapter= new AdminViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(tabLayout,viewPager2,((tab, position)->tab.setText(titles[position]))).attach();

        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(2));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.8f; // e.g. 0.5f
        layout.setLayoutParams(layoutParams);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==2){
                    addMenuBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                addMenuBtn.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        addMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMenuDialog = new Dialog(AdminMainPage.this);
                addMenuDialog.setContentView(R.layout.dialog_add_item);
                EditText menuCodeText= addMenuDialog.findViewById(R.id.menuCodeText);
                EditText menuNameText= addMenuDialog.findViewById(R.id.menuNameText);
                EditText menuPriceText= addMenuDialog.findViewById(R.id.menuPriceText);
                EditText menuDescriptionText= addMenuDialog.findViewById(R.id.menuDescriptionText);
                menuImage= addMenuDialog.findViewById(R.id.menuImage);
                Button addBtn= addMenuDialog.findViewById(R.id.addBtn);
                Button cancelBtn= addMenuDialog.findViewById(R.id.cancelReservationBtn);
                TextView chooseImageText= addMenuDialog.findViewById(R.id.chooseImageText);
                addMenuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addMenuDialog.show();


                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addMenuDialog.dismiss();
                    }
                });

                chooseImageText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menuCode= menuCodeText.getText().toString();

                        if(TextUtils.isEmpty(menuCode)){
                            menuCodeText.setError("Menu code is required before choosing image");
                            return;
                        }

                        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(openGalleryIntent,1000);
                    }
                });


                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menuCode = menuCodeText.getText().toString();
                        String name= menuNameText.getText().toString();
                        String price=menuPriceText.getText().toString();
                        String des= menuDescriptionText.getText().toString();


                        if(TextUtils.isEmpty(menuCode)){
                            menuCodeText.setError("Code is required");
                            return;
                        }
                        if(TextUtils.isEmpty(name)){
                            menuNameText.setError("Menu name is required");
                            return;
                        }
                        if(TextUtils.isEmpty(price)){
                            menuPriceText.setError("Menu price is required");
                            return;
                        }
                        if(TextUtils.isEmpty(des)){
                            menuDescriptionText.setError("Menu description is required");
                            return;
                        }
                        Menu menu = new Menu(menuCode,name,price,des,imageLink);
                        DocumentReference documentReference =fStore.collection("restaurant").document(RESTAURANT_OF_ADMIN).collection("Menu").document(menuCode);
                        documentReference.set(menu).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminMainPage.this, "Menu "+menuCode+" is created", Toast.LENGTH_SHORT).show();
                                addMenuDialog.dismiss();
                            }
                        });




                    }
                });

            }
        });



    }



    public void init(){
        backBtn=findViewById(R.id.adminBackBtn);
        viewPager2=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        addMenuBtn=findViewById(R.id.addMenuBtn);
        storageReference= FirebaseStorage.getInstance().getReference();
        fStore=FirebaseFirestore.getInstance();
        adminRestaurant=findViewById(R.id.adminRestaurant);
        adminLogo=findViewById(R.id.adminLogo);
        restaurantName=findViewById(R.id.restaurantName);


        DocumentReference documentReference=fStore.collection("restaurant").document(RESTAURANT_OF_ADMIN);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                restaurantImage= value.getString("RestaurantAdminImage");
                restaurantLogo= value.getString("RestaurantLogo");
                restaurantName.setText(RESTAURANT_OF_ADMIN);
                Picasso.get().load(restaurantImage).into(adminRestaurant);
                Picasso.get().load(restaurantLogo).into(adminLogo);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&&resultCode== Activity.RESULT_OK){

            try{
                Uri imageUri =data.getData();
                uploadImageToFirebase(imageUri);

            }catch(NullPointerException ex){

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //Upload image to firebase
        StorageReference fileRef= storageReference.child("menu/"+menuCode+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(menuImage);
                        imageLink=uri.toString();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminMainPage.this, "Upload Image Fail, Please Try Again ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}