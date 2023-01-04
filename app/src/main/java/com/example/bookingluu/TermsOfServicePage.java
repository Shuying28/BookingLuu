package com.example.bookingluu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TermsOfServicePage extends AppCompatActivity {
    private ImageView termOfServiceBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service_page);
        termOfServiceBackBtn= findViewById(R.id.termOfServiceBackBtn);
        termOfServiceBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }
}