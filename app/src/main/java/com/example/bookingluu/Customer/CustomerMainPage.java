package com.example.bookingluu.Customer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bookingluu.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomerMainPage extends AppCompatActivity {
    private TextView restaurant_name, restaurant_address, end_operation_hour, showReview,operation_status, open_operation_hour;
    ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    private String[] titles= new String[]{"RESERVATION","MENU","RATING","DETAILS"};
    private Button addReviewBtn,backBtn ;
    private Dialog addRatingDialog;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId,currentUserName,currentUserImage;
    String numberOfRating;
    String currentRating;
    DocumentReference documentReference;
    DocumentReference documentReferenceToRating;
    // To get what is the current restaurant
    private String currentVisitRestaurant;
    String restaurantImageLink;
    private ImageView restaurantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main_page);


        init();
        viewPager2.setTag(currentVisitRestaurant);

        documentReference= fStore.collection("restaurant").document(currentVisitRestaurant);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                restaurant_name.setText(value.getString("RestaurantName"));
                restaurant_address.setText(value.getString("Address"));
                end_operation_hour.setText(value.getString("EndOperationHour"));

                //rating in 2 decimal places
                String ratingvalues = value.getString("currentRating");
                double double_ratingvalues = Double.valueOf(ratingvalues);
                String strDouble = String.format("%.2f", double_ratingvalues);
                showReview.setText(strDouble);

                //update operation hour
                String open_operation_hour = value.getString("OpenOperationHour");
                String end_operation_hour=value.getString("EndOperationHour");
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                String timeStr2 = formatter.format(Calendar.getInstance().getTime());
                try{
                Date openTime = formatter.parse(open_operation_hour);
                Date closeTime = formatter.parse(end_operation_hour);
                Date currentTime = formatter.parse(timeStr2);
                    Boolean close = openTime.after(currentTime)||currentTime.after(closeTime);
                    Boolean open = openTime.before(currentTime)&&currentTime.before(closeTime);
                    Boolean bool3 = openTime.equals(currentTime);

                    System.out.println("gagaeqgeagearg"+ openTime);
                    System.out.println("fawaegwergherhqeghqeh"+ currentTime);
                    System.out.println("gagaeqgeagearg"+ closeTime);
                    System.out.println("fawaegwergherhqeghqeh"+ close);
                    if(close){
                        operation_status.setText("Now Closed");
                        operation_status.setTextColor(getApplicationContext().getResources().getColor(R.color.decline_colour));
                    }else if(open){
                        operation_status.setText("Now Open");
                    }else if(bool3){
                        operation_status.setText("Close Soon");
                        operation_status.setTextColor(getApplicationContext().getResources().getColor(R.color.orange_second));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RestaurantListPage.class));
            }
        });

        viewPagerFragmentAdapter= new ViewPagerFragmentAdapter(this);
        viewPager2.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(tabLayout,viewPager2,((tab, position)->tab.setText(titles[position]))).attach();
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 1.5f; // e.g. 0.5f
        layout.setLayoutParams(layoutParams);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==2){
                    addReviewBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                addReviewBtn.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRatingDialog = new Dialog(CustomerMainPage.this);
                addRatingDialog.setContentView(R.layout.add_rating_dialog);
                EditText commentText= addRatingDialog.findViewById(R.id.commentText);
                Button postBtn= addRatingDialog.findViewById(R.id.postBtn);
                Button cancelBtn= addRatingDialog.findViewById(R.id.cancelReservationBtn);
                RatingBar ratingBar= addRatingDialog.findViewById(R.id.ratingBar);
                addRatingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addRatingDialog.show();

                postBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment=commentText.getText().toString();
                        String rate = String.valueOf(ratingBar.getRating());
                        Rating rating = new Rating(currentUserName,comment,currentUserImage,rate);
                        DocumentReference documentReference =fStore.collection("restaurant").document(currentVisitRestaurant).collection("RatingList").document(currentUserName+numberOfRating);
                        documentReference.set(rating);
                        updateRatingToFireStore(rate);
                        Toast.makeText(CustomerMainPage.this, "Rate Successful", Toast.LENGTH_SHORT).show();
                        addRatingDialog.dismiss();



                    }
                });


                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addRatingDialog.dismiss();
                    }
                });


            }
        });
    }

    public void init(){
        restaurant_name=findViewById(R.id.restaurantName);
        restaurant_address=findViewById(R.id.restaurant_address);
        end_operation_hour=findViewById(R.id.EndOperationHour);
        showReview=findViewById(R.id.showReview);
        operation_status=findViewById(R.id.Operation_status);
        viewPager2=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        addReviewBtn=findViewById(R.id.addReviewBtn);
        backBtn=findViewById(R.id.backBtn);
        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        restaurantImage=findViewById(R.id.restaurantImage);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            currentVisitRestaurant=extras.getString("RestaurantName");
        }
        documentReference= fStore.collection("customers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUserName= value.getString("fullName");
                currentUserImage=value.getString("image");

            }
        });

        documentReferenceToRating=fStore.collection("restaurant").document(currentVisitRestaurant);
        documentReferenceToRating.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                numberOfRating= value.getString("numberOfRating");
                currentRating=value.getString("currentRating");
                restaurantImageLink=value.getString("RestaurantImage");
                Picasso.get().load(restaurantImageLink).into(restaurantImage);
            }
        });

    }

    public void updateRatingToFireStore(String rate){
        double temp= Double.parseDouble(rate);
        double totalRate=Double.parseDouble(currentRating)*Integer.parseInt(numberOfRating)+temp;
        String res=String.valueOf(totalRate/(Integer.parseInt(numberOfRating)+1));
        documentReferenceToRating.update("numberOfRating",String.valueOf(Integer.parseInt(numberOfRating)+1));
        documentReferenceToRating.update("currentRating",res);

    }

    public String passStr(){
        return currentVisitRestaurant;
    }
}