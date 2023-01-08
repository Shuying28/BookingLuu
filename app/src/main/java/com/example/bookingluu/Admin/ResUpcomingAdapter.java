package com.example.bookingluu.Admin;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.internal.JsonUtil;

public class ResUpcomingAdapter extends RecyclerView.Adapter<ResUpcomingAdapter.MyViewHolder> {
    Context context;
    static ArrayList<Reservation> reservationArrayList;
    static Context temp;

    public ResUpcomingAdapter(Context context, ArrayList<Reservation> reservationArrayList) {
        this.context = context;
        this.reservationArrayList = reservationArrayList;

    }

    public void setFilterList(ArrayList<Reservation> filterList){
        this.reservationArrayList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResUpcomingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_upcoming, parent, false);
        temp = parent.getContext();
        return new ResUpcomingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResUpcomingAdapter.MyViewHolder holder, int position) {
        Reservation reservation = reservationArrayList.get(position);
        holder.upcomingNameText.setText(reservation.getCustomerName());
        holder.upcomingTableNoText.setText(String.valueOf(reservation.getTableNo()));
        holder.upcomingDateText.setText(reservation.getDate());
        holder.upcomingPaxText.setText(String.valueOf(reservation.getPax()));
        holder.upcomingTimeText.setText(reservation.getTime());
        holder.ResNoText.setText("#" + String.valueOf(reservation.getBookingNo()));
    }

    @Override
    public int getItemCount() {
        try {
            return reservationArrayList.size();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return 0;
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView upcomingNameText, upcomingTableNoText, upcomingDateText, upcomingPaxText, upcomingTimeText, ResNoText;
        Button upcomingArrivedBtn, upcomingCancelBtn;
        private static FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference;
        private final String RESTAURANT_OF_ADMIN= AdminLoginPage.restaurantOfAdmin;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            upcomingNameText = itemView.findViewById(R.id.upcomingNameText);
            upcomingTableNoText = itemView.findViewById(R.id.upcomingTableNoText);
            upcomingDateText = itemView.findViewById(R.id.upcomingDateText);
            upcomingPaxText = itemView.findViewById(R.id.upcomingPaxText);
            upcomingTimeText = itemView.findViewById(R.id.upcomingTimeText);
            ResNoText = itemView.findViewById(R.id.ResNoText);
            upcomingArrivedBtn = itemView.findViewById(R.id.upcomingArrivedBtn);
            upcomingCancelBtn = itemView.findViewById(R.id.upcomingCancelBtn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(temp);
                    bottomSheetDialog.setContentView(R.layout.admin_reservation_info);
                    TextView resInfoNameText = bottomSheetDialog.findViewById(R.id.resInfoNameText);
                    TextView resInfoPhoneNoText = bottomSheetDialog.findViewById(R.id.resInfoPhoneNoText);
                    TextView resInfoEmailText = bottomSheetDialog.findViewById(R.id.resInfoEmailText);
                    TextView resInfoDateText = bottomSheetDialog.findViewById(R.id.resInfoDateText);
                    TextView resInfoTimeText = bottomSheetDialog.findViewById(R.id.resInfoTimeText);
                    TextView resInfoPaxText = bottomSheetDialog.findViewById(R.id.resInfoPaxText);
                    TextView resInfoTableNoText = bottomSheetDialog.findViewById(R.id.resInfoTableNoText);
                    TextView resInfoOrderNoText = bottomSheetDialog.findViewById(R.id.resInfoOrderNoText);
                    TextView resInfoFoodText = bottomSheetDialog.findViewById(R.id.resInfoFoodText);
                    TextView resInfoNotesText = bottomSheetDialog.findViewById(R.id.resInfoNotesText);

                    Reservation selectedReservation = reservationArrayList.get(getAdapterPosition());
                    CircleImageView adminResProfilePic = bottomSheetDialog.findViewById(R.id.adminResProfilePic);
                    resInfoNameText.setText(selectedReservation.getCustomerName());
                    resInfoPhoneNoText.setText(selectedReservation.getCustomerPhoneNo());
                    resInfoEmailText.setText(selectedReservation.getCustomerEmail());
                    resInfoDateText.setText(selectedReservation.getDate());
                    resInfoTimeText.setText(selectedReservation.getTime());
                    resInfoPaxText.setText(selectedReservation.getPax() + " people");
                    resInfoTableNoText.setText("Table No: " + selectedReservation.getTableNo());
                    resInfoOrderNoText.setText("#" + selectedReservation.getBookingNo());
                    resInfoFoodText.setText(selectedReservation.getFood());
                    resInfoNotesText.setText(selectedReservation.getCustomerNotes());
                    Picasso.get().load(selectedReservation.getImageURI()).into(adminResProfilePic);
                    bottomSheetDialog.show();
                }
            });

            upcomingArrivedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reservation selectedReservation = reservationArrayList.get(getAdapterPosition());
                    documentReference=fStore.collection("restaurant").document(RESTAURANT_OF_ADMIN).collection("Reservation")
                            .document(String.valueOf(selectedReservation.getBookingNo()));
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            documentReference.update("status", "Arrived");
                            try{
                                reservationArrayList.remove(getAdapterPosition());
                            }catch(IndexOutOfBoundsException e){

                            }
                            //Todo : send email to customer
                        }
                    });
                }
            });

            upcomingCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reservation selectedReservation = reservationArrayList.get(getAdapterPosition());
                    documentReference=fStore.collection("restaurant").document(RESTAURANT_OF_ADMIN).collection("Reservation")
                            .document(String.valueOf(selectedReservation.getBookingNo()));
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            documentReference.update("status", "Cancel");
                            reservationArrayList.remove(getAdapterPosition());
                            //Todo : send email to customer
                        }
                    });
                }
            });
        }
    }
}
