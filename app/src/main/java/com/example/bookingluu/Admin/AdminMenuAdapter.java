package com.example.bookingluu.Admin;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingluu.Customer.RestaurantListPage;
import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Menu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminMenuAdapter extends RecyclerView.Adapter<com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder> {
    public static Context context;
    public static ArrayList<Menu> menuArrayList;
    Dialog deleteDialog;
    static Context temp;


    public AdminMenuAdapter(Context context, ArrayList<Menu> menuArrayList) {
        this.context = context;
        this.menuArrayList = menuArrayList;
    }

    @NonNull
    @Override
    public com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.admin_menu_card_view,parent,false);
        temp= parent.getContext();

//        // TODO: show dialog
//        final MyViewHolder vHolder= new MyViewHolder(v);
//        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dialog d= new Dialog(parent.getContext());
//                d.setContentView(R.layout.dialog_delete_item);
//                try {
//                    d.show();
//
//                }
//                catch (WindowManager.BadTokenException ex) {
//                    ex.printStackTrace();
//                }
//
//            }
//        });
//        return vHolder;
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


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView menuCodeText,menuNameText,menuDesText,menuPriceText;
        ShapeableImageView menuImage;
        ImageView adminCardMenu;
        static int selectedPosition;
        private static FirebaseFirestore fStore= FirebaseFirestore.getInstance();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menuCodeText=itemView.findViewById(R.id.cardMenuCodeText);
            menuNameText=itemView.findViewById(R.id.cardMenuNameText);
            menuDesText=itemView.findViewById(R.id.cardMenuDesText);
            menuPriceText=itemView.findViewById(R.id.cardMenuPriceText);
            menuImage=itemView.findViewById(R.id.cardMenuImage);
            adminCardMenu=itemView.findViewById(R.id.adminCardMenu);
            adminCardMenu.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            showPopUpMenu(view);
            selectedPosition=getAdapterPosition();


        }
        public static void showPopUpMenu(View view){
            PopupMenu popupMenu= new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pop_up_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menuDelete:
                            Toast.makeText(context, "hhhh", Toast.LENGTH_SHORT).show();
                            Dialog deleteMenuDialog= new Dialog(temp);
                            deleteMenuDialog.setContentView(R.layout.dialog_delete_item);
                            deleteMenuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            deleteMenuDialog.show();
                            Button deleteMenuBtn= deleteMenuDialog.findViewById(R.id.deleteMenuBtn);
                            Button cancelDeleteMenuBtn = deleteMenuDialog.findViewById(R.id.cancelDeleteMenuBtn);
                            cancelDeleteMenuBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deleteMenuDialog.dismiss();
                                }
                            });
                            deleteMenuBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Menu menuToBeDelete= menuArrayList.get(selectedPosition);
                                    System.out.println("uhagweaggqw"+menuToBeDelete.getMenuCode());
                                    menuArrayList.remove(selectedPosition);
                                    fStore.collection("restaurant").document("HollandFood").collection("Menu")
                                            .document(menuToBeDelete.getMenuCode()).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    deleteMenuDialog.dismiss();
                                                    Toast.makeText(context, "Menu "+menuToBeDelete.getMenuCode()+" deleted", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });

                                }
                            });


                            return true;
                        case R.id.menuEdit:
                            Dialog editMenuDialog= new Dialog(temp);
                            editMenuDialog.setContentView(R.layout.dialog_edit_item);
                            editMenuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            editMenuDialog.show();

                            //Todo : edit menu code here (regina)

                            return true;

                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();

        }



    }
}
