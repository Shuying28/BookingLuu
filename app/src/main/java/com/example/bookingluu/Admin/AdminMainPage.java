package com.example.bookingluu.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.bookingluu.Customer.ViewPagerFragmentAdapter;
import com.example.bookingluu.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminMainPage extends AppCompatActivity {

    AdminViewPagerAdapter viewPagerFragmentAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    private String[] titles= new String[]{"PENDING","UPCOMING","MENU"};
    private Button addReviewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        init();


        viewPagerFragmentAdapter= new AdminViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(tabLayout,viewPager2,((tab, position)->tab.setText(titles[position]))).attach();

        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(2));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.8f; // e.g. 0.5f
        layout.setLayoutParams(layoutParams);


//
//        LinearLayout layout2 = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1));
//        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layout2.getLayoutParams();
//        layoutParams.weight = 1.3f; // e.g. 0.5f
//        layout2.setLayoutParams(layoutParams);
    }

    public void init(){
        viewPager2=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        addReviewBtn=findViewById(R.id.addReviewBtn);
    }
}