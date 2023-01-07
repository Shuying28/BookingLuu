package com.example.bookingluu.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookingluu.Customer.Rating;
import com.example.bookingluu.Customer.RatingAdapter;
import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Reservation;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PendingFragment extends Fragment {
    private RecyclerView resPendingList;
    private FirebaseFirestore fStore;
    private ResPendingAdapter ResPendingAdapter;
    ArrayList<Reservation> reservationArrayList;
    ProgressDialog progressDialog;
    private final String RESTAURANT_OF_ADMIN= AdminLoginPage.restaurantOfAdmin;

    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resPendingList=view.findViewById(R.id.resPendingList);
        fStore= FirebaseFirestore.getInstance();
        reservationArrayList= new ArrayList<>();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data......");
        progressDialog.show();



        resPendingList.setHasFixedSize(true);
        resPendingList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        ResPendingAdapter =new ResPendingAdapter(getActivity().getApplicationContext(),reservationArrayList);

        resPendingList.setAdapter(ResPendingAdapter);
        EventChangeListener();


    }

    private void EventChangeListener() {
        fStore.collection("restaurant").document(RESTAURANT_OF_ADMIN).collection("Reservation").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        Reservation reservation= dc.getDocument().toObject(Reservation.class);
                        try{
                            if(reservation.getStatus().equals("Pending")){
                                reservationArrayList.add(reservation);}
                        }catch(NullPointerException e){

                        }

                    }
                    ResPendingAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())progressDialog.dismiss();
                }
            }
        });

    }
}