package com.example.bookingluu.Admin;


import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingluu.Customer.RestaurantListPage;
import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMenuAdapter extends RecyclerView.Adapter<com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder> {
    public static Context context;
    public static ArrayList<Menu> menuArrayList;
    Dialog deleteDialog;
    static Context temp;
    static String imageLink;
    static String menuCodeTxt;
    static StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public static final int PICK_IMAGE = 1;
    private final String RESTAURANT_OF_ADMIN= AdminLoginPage.restaurantOfAdmin;


    public AdminMenuAdapter(Context context, ArrayList<Menu> menuArrayList) {
        this.context = context;
        this.menuArrayList = menuArrayList;
    }

    @NonNull
    @Override
    public com.example.bookingluu.Admin.AdminMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_menu_card_view, parent, false);
        temp = parent.getContext();

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
        TextView menuCodeText, menuNameText, menuDesText, menuPriceText;
        ShapeableImageView menuImage;
        ImageView adminCardMenu;
        static int selectedPosition;
        private static FirebaseFirestore fStore = FirebaseFirestore.getInstance();


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menuCodeText = itemView.findViewById(R.id.cardMenuCodeText);
            menuNameText = itemView.findViewById(R.id.cardMenuNameText);
            menuDesText = itemView.findViewById(R.id.cardMenuDesText);
            menuPriceText = itemView.findViewById(R.id.cardMenuPriceText);
            menuImage = itemView.findViewById(R.id.cardMenuImage);
            adminCardMenu = itemView.findViewById(R.id.adminCardMenu);
            adminCardMenu.setOnClickListener(this);


        }



        @Override
        public void onClick(View view) {
            showPopUpMenu(view);
            selectedPosition = getAdapterPosition();


        }

        public static void showPopUpMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pop_up_menu);
            final String RESTAURANT_OF_ADMIN= AdminLoginPage.restaurantOfAdmin;

            ActivityResultLauncher<String> launcher;
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menuDelete:
                            Dialog deleteMenuDialog = new Dialog(temp);
                            deleteMenuDialog.setContentView(R.layout.dialog_delete_item);
                            deleteMenuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            deleteMenuDialog.show();

                            Button deleteMenuBtn = deleteMenuDialog.findViewById(R.id.deleteMenuBtn);
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
                                    Menu menuToBeDelete = menuArrayList.get(selectedPosition);
                                    menuArrayList.remove(selectedPosition);
                                    fStore.collection("restaurant").document(RESTAURANT_OF_ADMIN).collection("Menu")
                                            .document(menuToBeDelete.getMenuCode()).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    deleteMenuDialog.dismiss();
                                                    Toast.makeText(context, "Menu " + menuToBeDelete.getMenuCode() + " deleted", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Menu " + menuToBeDelete.getMenuCode() + " failed to delete", Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                }
                            });


                            return true;

                        case R.id.menuEdit:
                            Dialog editMenuDialog = new Dialog(temp);
                            editMenuDialog.setContentView(R.layout.dialog_edit_item);
                            editMenuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            editMenuDialog.show();

                            Menu menuToBeEdited = menuArrayList.get(selectedPosition);

                            Button saveMenuBtn = editMenuDialog.findViewById(R.id.saveEditMenuBtn);
                            Button cancelEditMenuBtn = editMenuDialog.findViewById(R.id.cancelEditMenuBtn);
                            TextView menuCode = editMenuDialog.findViewById(R.id.menuCodeEditText);
                            EditText menuName = editMenuDialog.findViewById(R.id.menuNameEditText);
                            EditText menuPrice = editMenuDialog.findViewById(R.id.menuPriceEditText);
                            EditText menuDesc = editMenuDialog.findViewById(R.id.menuDesEditText);
                            ShapeableImageView menuImage = editMenuDialog.findViewById(R.id.menuEditImage);

                            menuCode.setText(menuToBeEdited.getMenuCode());
                            menuName.setText(menuToBeEdited.getMenuName());
                            menuPrice.setText(menuToBeEdited.getMenuPrice());
                            menuDesc.setText(menuToBeEdited.getMenuDescription());
                            Picasso.get().load(menuToBeEdited.getMenuImage()).into(menuImage);


                            cancelEditMenuBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    editMenuDialog.dismiss();
                                }
                            });


                            menuCode.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    menuCode.setError("This field cannot be edited!");
                                    Toast.makeText(temp, "Menu code is uneditable", Toast.LENGTH_SHORT).show();


                                }
                            });

                            menuImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(temp, "Menu image is uneditable", Toast.LENGTH_SHORT).show();
                                }
                            });

                            saveMenuBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view){
                                    String code = menuCode.getText().toString();
                                    String name = menuName.getText().toString();
                                    String price = menuPrice.getText().toString();
                                    String des = menuDesc.getText().toString();
                                    String image = "";

                                    if(TextUtils.isEmpty(name)){
                                        menuName.setError("Menu name is required");
                                        return;
                                    }
                                    if(TextUtils.isEmpty(price)){
                                        menuPrice.setError("Menu price is required");
                                        return;
                                    }
                                    if(TextUtils.isEmpty(des)){
                                        menuDesc.setError("Menu description is required");
                                        return;
                                    }


                                    Menu updatedMenu = new Menu(code, name, price, des, menuToBeEdited.getMenuImage());

                                    fStore.collection("restaurant").document(RESTAURANT_OF_ADMIN).collection("Menu").document(code)
                                            .set(updatedMenu)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                editMenuDialog.dismiss();
                                                Toast.makeText(context, "Menu " + code + " Successfully Updated!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Menu " + code + " Update Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                }


                            });


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
