package com.example.bookingluu.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bookingluu.Customer.RestaurantListPage;
import com.example.bookingluu.R;

public class AdminLoginPage extends AppCompatActivity {
    private Button adminLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        init();
        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainPage.class));
            }
        });
    }


    public void init(){
        adminLoginBtn=findViewById(R.id.adminLoginBtn);
    }
}