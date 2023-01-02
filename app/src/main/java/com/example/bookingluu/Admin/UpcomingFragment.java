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

import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Reservation;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class UpcomingFragment extends Fragment {
    private RecyclerView resUpcomingList;
    private FirebaseFirestore fStore;
    private ResUpcomingAdapter ResUpcomingAdapter;
    ArrayList<Reservation> reservationArrayList;
    ProgressDialog progressDialog;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resUpcomingList=view.findViewById(R.id.resUpcomingList);
        fStore= FirebaseFirestore.getInstance();
        reservationArrayList= new ArrayList<>();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data......");
        progressDialog.show();



        resUpcomingList.setHasFixedSize(true);
        resUpcomingList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        ResUpcomingAdapter =new ResUpcomingAdapter(getActivity().getApplicationContext(),reservationArrayList);

        resUpcomingList.setAdapter(ResUpcomingAdapter);
        EventChangeListener();


    }

    private void EventChangeListener() {
        fStore.collection("restaurant").document("HollandFood").collection("Reservation").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED||dc.getType()==DocumentChange.Type.MODIFIED){
                        Reservation reservation= dc.getDocument().toObject(Reservation.class);
                        if(reservation.getStatus().equals("Accepted")){
                            reservationArrayList.add(reservation);
                        }
                    }

                    ResUpcomingAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())progressDialog.dismiss();
                }
            }
        });

    }
}