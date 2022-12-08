package com.example.bookingluu.Customer;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    private String[] titles= new String[]{"RESERVATION","MENU","RATING","DETAILS"};
    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ReservationFragment();
            case 1:
                return new MenuFragment();
            case 2:
                return new RatingFragment();
            case 3:
                return new DetailsFragment();
        }
        return new ReservationFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
