package com.example.bookingluu.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Reservation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<Reservation> historyArrayList;

    public HistoryAdapter(Context context, ArrayList<Reservation> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reservation_history,parent,false);

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
        ImageView restaurantImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.upcomingDateText);
            code=itemView.findViewById(R.id.code);
            restaurantName=itemView.findViewById(R.id.restaurantName);
            people=itemView.findViewById(R.id.people);
            hours=itemView.findViewById(R.id.hours);
            status=itemView.findViewById(R.id.status);
            restaurantImage=itemView.findViewById(R.id.restaurantImage);


        }
    }
}
