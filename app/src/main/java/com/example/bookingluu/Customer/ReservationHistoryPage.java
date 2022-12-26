package com.example.bookingluu.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.R;

public class ReservationHistoryPage extends AppCompatActivity {
    private ImageView historyBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_history_page);
        init();

        historyBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), CustomerProfilePage.class));
            }
        });
    }

    public void init(){
        historyBackBtn=findViewById(R.id.historyBackBtn);
    }
}