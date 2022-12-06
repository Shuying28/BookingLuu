package com.example.bookingluu.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.R;

public class ForgotPasswordPage extends AppCompatActivity {
    private ImageView fPasswordBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);
        init();

        fPasswordBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));
            }
        });
    }

    public void init(){
        fPasswordBackBtn=findViewById(R.id.fPasswordBackBtn);
    }
}