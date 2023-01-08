package com.example.bookingluu.Customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RestaurantListPage extends AppCompatActivity {
    private ImageView backBtn, profileBtn;
    private ShapeableImageView  hollandImage, thailandImage;
    private Dialog logoutDialog;
    private Button tempBtn,visitHollandBtn,visitThaiBtn;
    private TextView hollFoodRating, hollFoodNumRating, thaiFoodRating, thaiFoodNumRating;
    FirebaseFirestore firebaseFirestore;
    public static String passString;

    String url1 = "https://firebasestorage.googleapis.com/v0/b/bookingluu-b66f7.appspot.com/o/BookingLuu%20(1).png?alt=media&token=216ab6ac-520a-4ceb-b964-fcd484046f19";
    String url2 = "https://firebasestorage.googleapis.com/v0/b/bookingluu-b66f7.appspot.com/o/Frame%202157.png?alt=media&token=83c1347b-d333-45ce-ab24-0144799a5f3b";
    String url3 = "https://firebasestorage.googleapis.com/v0/b/bookingluu-b66f7.appspot.com/o/Thailand%20Food%20(1).png?alt=media&token=7c9ef088-8f02-41ad-9e89-be175e18fd84";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list_page);
        init();
        setHollandFood();
        setThailandFood();

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();


        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), CustomerProfilePage.class));
            }
        });



        visitHollandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHolland= new Intent(getApplicationContext(),CustomerMainPage.class);
                toHolland.putExtra("RestaurantName", "HollandFood");
                passString = "HollandFood";
                startActivity(toHolland);


            }
        });


        visitThaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toThailand= new Intent(getApplicationContext(),CustomerMainPage.class);
                toThailand.putExtra("RestaurantName", "ThailandFood");
                passString="ThailandFood";
                startActivity(toThailand);
            }
        });
    }


    @Override
    public void onBackPressed() {
        logoutDialog = new Dialog(RestaurantListPage.this);
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

    private void init(){
        profileBtn=findViewById(R.id.profileBtn);
        visitHollandBtn=findViewById(R.id.visitHollandBtn);
        visitThaiBtn=findViewById(R.id.visitThaiBtn);
        hollFoodRating=findViewById(R.id.hollFoodRating);
        hollFoodNumRating=findViewById(R.id.hollFoodNumRating);
        thaiFoodRating=findViewById(R.id.thaiFoodRating);
        thaiFoodNumRating=findViewById(R.id.thaiFoodNumRating);
        firebaseFirestore= FirebaseFirestore.getInstance();
        hollandImage= findViewById(R.id.hollandImage);
        thailandImage=findViewById(R.id.thailandImage);
    }

    private void setHollandFood(){
        DocumentReference documentReference;
        documentReference=firebaseFirestore.collection("restaurant").document("HollandFood");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String ratingvalues = value.getString("currentRating");
                double double_ratingvalues = Double.valueOf(ratingvalues);
                String strDouble = String.format("%.2f", double_ratingvalues);
                hollFoodRating.setText(strDouble);
                hollFoodNumRating.setText("("+value.getString("numberOfRating")+")");
                Picasso.get().load(value.getString("RestaurantImage")).into(hollandImage);
            }
        });
    }

    private void setThailandFood(){
        DocumentReference documentReference;
        documentReference=firebaseFirestore.collection("restaurant").document("ThailandFood");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String ratingvalues = value.getString("currentRating");
                double double_ratingvalues = Double.valueOf(ratingvalues);
                String strDouble = String.format("%.2f", double_ratingvalues);
                thaiFoodRating.setText(strDouble);
                thaiFoodNumRating.setText("("+value.getString("numberOfRating")+")");
                Picasso.get().load(value.getString("RestaurantImage")).into(thailandImage);
            }
        });
    }




}