package com.example.bookingluu.Customer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.R;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RestaurantListPage extends AppCompatActivity {
    private ImageView backBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list_page);
        init();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));
                finish();

            }
        });
    }

    private void init(){
        backBtn=findViewById(R.id.backBtn);
        profileBtn=findViewById(R.id.profileBtn);
    }
}