package com.example.bookingluu.Customer;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Menu;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    Context context;
    ArrayList<Menu> menuArrayList;

    public MenuAdapter(Context context, ArrayList<Menu> menuArrayList) {
        this.context = context;
        this.menuArrayList = menuArrayList;
    }

    @NonNull
    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cardview_menu_list,parent,false);
        return new MenuAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MyViewHolder holder, int position) {
        Menu menu = menuArrayList.get(position);
        holder.menuCodeText.setText(menu.getMenuCode());
        holder.menuNameText.setText(menu.getMenuName());
        holder.menuDesText.setText(menu.getMenuDescription());
        holder.menuPriceText.setText(menu.getMenuPrice());
        if(!TextUtils.isEmpty(menu.getMenuImage())){
            Picasso.get().load(menu.getMenuImage()).into(holder.menuImage);
        }


    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView menuCodeText,menuNameText,menuDesText,menuPriceText;
        ShapeableImageView menuImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menuCodeText=itemView.findViewById(R.id.cardMenuCodeText);
            menuNameText=itemView.findViewById(R.id.cardMenuNameText);
            menuDesText=itemView.findViewById(R.id.cardMenuDesText);
            menuPriceText=itemView.findViewById(R.id.cardMenuPriceText);
            menuImage=itemView.findViewById(R.id.cardMenuImage);
        }
    }
}
