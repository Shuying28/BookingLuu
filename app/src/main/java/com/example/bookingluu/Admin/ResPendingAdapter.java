package com.example.bookingluu.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ResPendingAdapter extends RecyclerView.Adapter<ResPendingAdapter.MyViewHolder> {
    Context context;
    static ArrayList<Reservation> reservationArrayList;
    static Context temp;

    public ResPendingAdapter(Context context, ArrayList<Reservation> reservationArrayList) {
        this.context = context;
        this.reservationArrayList = reservationArrayList;
    }

    @NonNull
    @Override
    public ResPendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.admin_pending,parent,false);
        temp= parent.getContext();
        return new ResPendingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResPendingAdapter.MyViewHolder holder, int position) {
        Reservation reservation = reservationArrayList.get(position);
        holder.pendingNameText.setText(reservation.getCustomerName());
        holder.pendingTableNoText.setText(String.valueOf(reservation.getTableNo()));
        holder.pendingDateText.setText(reservation.getDate());
        holder.pendingPaxText.setText(String.valueOf(reservation.getPax()));
        holder.pendingTimeText.setText(reservation.getTime());
        holder.ResNoText.setText("#"+String.valueOf(reservation.getBookingNo()));



    }

    @Override
    public int getItemCount() {
        return reservationArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView pendingNameText,pendingTableNoText,pendingDateText,pendingPaxText,pendingTimeText,ResNoText;
        Button pendingAcceptBtn,pendingDeclineBtn;
        private static FirebaseFirestore fStore= FirebaseFirestore.getInstance();
        DocumentReference documentReference;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pendingNameText = itemView.findViewById(R.id.pendingNameText);
            pendingTableNoText=itemView.findViewById(R.id.pendingTableNoText);
            pendingDateText=itemView.findViewById(R.id.pendingDateText);
            pendingPaxText=itemView.findViewById(R.id.pendingPaxText);
            pendingTimeText=itemView.findViewById(R.id.pendingTimeText);
            ResNoText=itemView.findViewById(R.id.ResNoText);
            pendingAcceptBtn=itemView.findViewById(R.id.pendingAcceptBtn);
            pendingDeclineBtn=itemView.findViewById(R.id.pendingDeclineBtn);
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
                    CircleImageView adminResProfilePic = bottomSheetDialog.findViewById(R.id.adminResProfilePic);
                    Reservation selectedReservation = reservationArrayList.get(getAdapterPosition());
                    resInfoNameText.setText(selectedReservation.getCustomerName());
                    resInfoPhoneNoText.setText(selectedReservation.getCustomerPhoneNo());
                    resInfoEmailText.setText(selectedReservation.getCustomerEmail());
                    resInfoDateText.setText(selectedReservation.getDate());
                    resInfoTimeText.setText(selectedReservation.getTime());
                    resInfoPaxText.setText(selectedReservation.getPax()+" people");
                    resInfoTableNoText.setText("Table No: "+selectedReservation.getTableNo());
                    resInfoOrderNoText.setText("#"+selectedReservation.getBookingNo());
                    resInfoFoodText.setText(selectedReservation.getFood());
                    resInfoNotesText.setText(selectedReservation.getCustomerNotes());
                    Picasso.get().load(selectedReservation.getImageURI()).into(adminResProfilePic);
                    bottomSheetDialog.show();
                }
            });

            pendingAcceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reservation selectedReservation = reservationArrayList.get(getAdapterPosition());
                     documentReference=fStore.collection("restaurant").document("HollandFood").collection("Reservation")
                            .document(String.valueOf(selectedReservation.getBookingNo()));
                     documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                         @Override
                         public void onSuccess(DocumentSnapshot documentSnapshot) {
                             documentReference.update("status", "Accepted");
                             reservationArrayList.remove(getAdapterPosition());
                             //Todo : send email to customer
                         }
                     });
                }
            });

            pendingDeclineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reservation selectedReservation = reservationArrayList.get(getAdapterPosition());
                    documentReference=fStore.collection("restaurant").document("HollandFood").collection("Reservation")
                            .document(String.valueOf(selectedReservation.getBookingNo()));
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            documentReference.update("status", "Declined");
                            reservationArrayList.remove(getAdapterPosition());
                            //Todo : send email to customer
                        }
                    });
                }
            });
        }
    }
}
