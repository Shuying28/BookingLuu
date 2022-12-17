package com.example.bookingluu.Admin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Menu;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminMenuAdapter extends RecyclerView.Adapter<com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder> {
    Context context;
    ArrayList<Menu> menuArrayList;

    public AdminMenuAdapter(Context context, ArrayList<Menu> menuArrayList) {
        this.context = context;
        this.menuArrayList = menuArrayList;
    }

    @NonNull
    @Override
    public com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.admin_menu_card_view,parent,false);
        return new com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder holder, int position) {
        Menu menu = menuArrayList.get(position);
        holder.menuCodeText.setText(menu.getMenuCode());
        holder.menuNameText.setText(menu.getMenuName());
        holder.menuDesText.setText(menu.getMenuDescription());
        holder.menuPriceText.setText(menu.getMenuPrice());
        holder.adminCardMenu.setImageResource(R.drawable.ic_baseline_more_vert_24);
        Picasso.get().load(menu.getMenuImage()).into(holder.menuImage);

    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView menuCodeText,menuNameText,menuDesText,menuPriceText;
        ShapeableImageView menuImage;
        ImageView adminCardMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menuCodeText=itemView.findViewById(R.id.cardMenuCodeText);
            menuNameText=itemView.findViewById(R.id.cardMenuNameText);
            menuDesText=itemView.findViewById(R.id.cardMenuDesText);
            menuPriceText=itemView.findViewById(R.id.cardMneuPriceText);
            menuImage=itemView.findViewById(R.id.cardMenuImage);
            adminCardMenu=itemView.findViewById(R.id.adminCardMenu);
            adminCardMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showPopUpMenu(view);

        }
        private void showPopUpMenu(View view){
            PopupMenu popupMenu= new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pop_up_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menuDelete:
                    return true;
                case R.id.menuEdit:

                    return true;

                default:
                    return false;
            }
        }
    }
}
