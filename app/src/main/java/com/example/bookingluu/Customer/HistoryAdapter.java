package com.example.bookingluu.Customer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Reservation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    static ArrayList<Reservation> historyArrayList;
    static Context temp;
    final String CURRENT_RESTAURANT= RestaurantListPage.passString;

    public HistoryAdapter(Context context, ArrayList<Reservation> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reservation_history,parent,false);
        temp= parent.getContext();
        return new HistoryAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        Reservation reservationHistory = historyArrayList.get(position);
        holder.date.setText(reservationHistory.getDate());
        holder.code.setText(("#"+reservationHistory.getBookingNo()));
        holder.restaurantName.setText(reservationHistory.getRestaurantName());
        holder.people.setText((reservationHistory.getPax())+" people, ");
        holder.hours.setText(reservationHistory.getTime());
        holder.status.setText(reservationHistory.getStatus());

        switch (reservationHistory.getStatus()){
            case "Accepted":
                holder.status.setBackgroundColor(context.getResources().getColor(R.color.admin_third));
                break;
            case "Pending":
                holder.status.setBackgroundColor(context.getResources().getColor(R.color.pending_colour));
                break;
            case "Arrived":
                holder.status.setBackgroundColor(context.getResources().getColor(R.color.approved_colour));
                break;
            case "Cancel":
                holder.status.setBackgroundColor(context.getResources().getColor(R.color.decline_colour));
                break;
        }


        //Picasso.get().load(menu.getMenuImage()).into(holder.menuImage);
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,code,restaurantName,people,hours,status;
        ImageView restaurantImage, resHisBackBtn;
        Button cancelReservationBtn,resHisbackBtn;
        private static FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference;
        final String CURRENT_RESTAURANT= RestaurantListPage.passString;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.upcomingDateText);
            code=itemView.findViewById(R.id.code);
            restaurantName=itemView.findViewById(R.id.restaurantName);
            people=itemView.findViewById(R.id.people);
            hours=itemView.findViewById(R.id.hours);
            status=itemView.findViewById(R.id.status);
            restaurantImage=itemView.findViewById(R.id.restaurantImage);
            resHisBackBtn = itemView.findViewById(R.id.resHisbackBtn);
            cancelReservationBtn = itemView.findViewById(R.id.cancelReservationBtn);
            resHisbackBtn = itemView.findViewById(R.id.resHisbackBtn);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(temp);
                    bottomSheetDialog.setContentView(R.layout.dialog_big_cancel_reservation);
                    TextView restaurantName = bottomSheetDialog.findViewById(R.id.restaurantName);
                    TextView restaurant_address = bottomSheetDialog.findViewById(R.id.restaurant_address);
                    TextView dateAndDay = bottomSheetDialog.findViewById(R.id.dateAndDay);
                    TextView hour = bottomSheetDialog.findViewById(R.id.hour);
                    TextView people = bottomSheetDialog.findViewById(R.id.people);
                    TextView orderMeal = bottomSheetDialog.findViewById(R.id.orderMeal);
                    TextView table = bottomSheetDialog.findViewById(R.id.table);
                    TextView notes = bottomSheetDialog.findViewById(R.id.notes);
                    Button cancelReservationBtn = bottomSheetDialog.findViewById(R.id.cancelReservationBtn);
                    Reservation reservationHistory = historyArrayList.get(getAdapterPosition());
                    ImageView reservationAccIcon = bottomSheetDialog.findViewById(R.id.reservationAccIcon);
                    TextView reservationStatusText = bottomSheetDialog.findViewById(R.id.reservationStatusText);
                    ImageView resHisbackBtn = bottomSheetDialog.findViewById(R.id.resHisbackBtn);

                    restaurantName.setText(reservationHistory.getRestaurantName());
                    restaurant_address.setText(reservationHistory.getRestaurantAddress());
                    dateAndDay.setText(reservationHistory.getDate());
                    hour.setText(reservationHistory.getTime());
                    people.setText(reservationHistory.getPax()+" people");
                    orderMeal.setText(reservationHistory.getFood());
                    table.setText("Table No: "+reservationHistory.getTableNo());
                    notes.setText(reservationHistory.getCustomerNotes());
                    bottomSheetDialog.show();

                    resHisbackBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.dismiss();
                        }
                    });

                    if(reservationHistory.getStatus().equals("Accepted")||reservationHistory.getStatus().equals("Pending")){
                        cancelReservationBtn.setVisibility(View.VISIBLE);
                        cancelReservationBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog cancelResConfirm = new Dialog(temp);
                                cancelResConfirm.setContentView(R.layout.dialog_cancel_reservation);
                                cancelResConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                Button confirmCancelBtn= cancelResConfirm.findViewById(R.id.confirmCancelBtn);
                                Button noCancelBtn = cancelResConfirm.findViewById(R.id.noCancelBtn);
                                cancelResConfirm.show();

                                confirmCancelBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Reservation reservationHistory = historyArrayList.get(getAdapterPosition());
                                        documentReference=fStore.collection("restaurant").document(CURRENT_RESTAURANT).collection("Reservation")
                                                .document(String.valueOf(reservationHistory.getBookingNo()));
                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                //TODO : card view arrangement
                                                documentReference.update("status", "Cancel");
//                                                historyArrayList.remove(getAdapterPosition());
                                                cancelResConfirm.dismiss();
                                                reservationAccIcon.setImageResource(R.drawable.cancelicon);
                                                reservationStatusText.setText("Your reservation is cancelled!");
                                                reservationStatusText.setTextColor(temp.getResources().getColor(R.color.decline_colour));

                                            }
                                        });
                                    }
                                });

                                noCancelBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cancelResConfirm.dismiss();
                                    }
                                });

                            }
                        });
                    }
                    if(reservationHistory.getStatus().equals("Cancel")){
                        reservationAccIcon.setImageResource(R.drawable.cancelicon);
                        reservationStatusText.setText("Your reservation is cancelled!");
                        reservationStatusText.setTextColor(temp.getResources().getColor(R.color.decline_colour));



                    }else if(reservationHistory.getStatus().equals("Pending")) {
                        reservationAccIcon.setImageResource(R.drawable.pendingicon);
                        reservationStatusText.setText("Your reservation is pending!");
                        reservationStatusText.setTextColor(temp.getResources().getColor(R.color.pending_colour));

                    }else if(reservationHistory.getStatus().equals("Accepted")) {
                        reservationAccIcon.setImageResource(R.drawable.green_tick);
                        reservationStatusText.setText("Your reservation is accepted!");
                        reservationStatusText.setTextColor(temp.getResources().getColor(R.color.admin_third));

                    }else if(reservationHistory.getStatus().equals("Arrived")) {
                        reservationAccIcon.setImageResource(R.drawable.arrivedicon);
                        reservationStatusText.setText("Your reservation is completed!");
                        reservationStatusText.setTextColor(temp.getResources().getColor(R.color.approved_colour));
                    }
                    // TODO: the status of other condition

                }
            });


        }
    }
}
