package com.example.bookingluu.Admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookingluu.Customer.DetailsFragment;
import com.example.bookingluu.Customer.MenuFragment;
import com.example.bookingluu.Customer.RatingFragment;
import com.example.bookingluu.Customer.ReservationFragment;

public class AdminViewPagerAdapter extends FragmentStateAdapter {
    private String[] titles= new String[]{"PENDING","UPCOMING","MENU"};
    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PendingFragment();
            case 1:
                return new UpcomingFragment();
            case 2:
                return new AdminMenuFragment();

        }
        return new PendingFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
