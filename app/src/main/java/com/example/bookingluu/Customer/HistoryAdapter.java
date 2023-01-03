package com.example.bookingluu.Customer;

import android.content.Context;
import android.content.Intent;
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
        Button cancelReservationBtn;
        private static FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference;

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
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(temp);
                    bottomSheetDialog.setContentView(R.layout.dialog_big_cancel_reservation);
                    TextView restaurantName = bottomSheetDialog.findViewById(R.id.restaurantName);
//                    TextView restaurant_address = bottomSheetDialog.findViewById(R.id.restaurant_address);
                    TextView dateAndDay = bottomSheetDialog.findViewById(R.id.dateAndDay);
                    TextView hour = bottomSheetDialog.findViewById(R.id.hour);
                    TextView people = bottomSheetDialog.findViewById(R.id.people);
                    TextView orderMeal = bottomSheetDialog.findViewById(R.id.orderMeal);
                    TextView table = bottomSheetDialog.findViewById(R.id.table);
                    TextView notes = bottomSheetDialog.findViewById(R.id.notes);
                    Button cancelReservationBtn = bottomSheetDialog.findViewById(R.id.cancelReservationBtn);
                    Reservation reservationHistory = historyArrayList.get(getAdapterPosition());

                    restaurantName.setText(reservationHistory.getRestaurantName());
//                    restaurant_address.setText(reservationHistory.getRestaurantName());
                    dateAndDay.setText(reservationHistory.getDate());
                    hour.setText(reservationHistory.getTime());
                    people.setText(reservationHistory.getPax()+" people");
                    orderMeal.setText(reservationHistory.getFood());
                    table.setText("Table No: "+reservationHistory.getTableNo());
                    notes.setText(reservationHistory.getCustomerNotes());

                    if(reservationHistory.getStatus().equals("Accepted")){
                        bottomSheetDialog.show();
                        cancelReservationBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Reservation reservationHistory = historyArrayList.get(getAdapterPosition());
                                documentReference=fStore.collection("restaurant").document("HollandFood").collection("Reservation")
                                        .document(String.valueOf(reservationHistory.getBookingNo()));
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        documentReference.update("status", "Cancel");
                                        historyArrayList.remove(getAdapterPosition());

                                    }
                                });
                            }
                        });
                    }

                }
            });


        }
    }
}
