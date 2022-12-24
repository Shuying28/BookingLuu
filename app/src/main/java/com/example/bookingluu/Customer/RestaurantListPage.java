package com.example.bookingluu.Customer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.R;
import com.google.firebase.auth.FirebaseAuth;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RestaurantListPage extends AppCompatActivity {
    private ImageView backBtn, profileBtn;
    private Dialog logoutDialog;
    private Button tempBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list_page);
        init();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog = new Dialog(RestaurantListPage.this);
                logoutDialog.setContentView(R.layout.dialog_customer_logout);
                Button yesBtn= logoutDialog.findViewById(R.id.yesBtn);
                Button noBtn= logoutDialog.findViewById(R.id.noBtn);
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

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), CustomerMainPage.class));
            }
        });

        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerMainPage.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        logoutDialog = new Dialog(RestaurantListPage.this);
        logoutDialog.setContentView(R.layout.dialog_customer_logout);
        Button yesBtn= logoutDialog.findViewById(R.id.yesBtn);
        Button noBtn= logoutDialog.findViewById(R.id.noBtn);
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
        backBtn=findViewById(R.id.listBackBtn);
        profileBtn=findViewById(R.id.profileBtn);
        tempBtn=findViewById(R.id.tempBtn);
    }
}