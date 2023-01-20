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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Reservation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UpcomingFragment extends Fragment {
    private RecyclerView resUpcomingList;
    private FirebaseFirestore fStore;
    private ResUpcomingAdapter ResUpcomingAdapter;
    private ArrayList<Reservation> reservationArrayList;
    private ArrayList<Reservation> filterReservationArrayList;
    ProgressDialog progressDialog;
    private final String RESTAURANT_OF_ADMIN= AdminLoginPage.restaurantOfAdmin;
    private SearchView searchView;

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

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }


        });


        resUpcomingList.setHasFixedSize(true);
        resUpcomingList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        ResUpcomingAdapter =new ResUpcomingAdapter(getActivity().getApplicationContext(),reservationArrayList);

        resUpcomingList.setAdapter(ResUpcomingAdapter);
        EventChangeListener();


    }

    private void filterList(String newText) {
        filterReservationArrayList = new ArrayList<>();
        if(!newText.isEmpty()){
            for (Reservation r : reservationArrayList) {
                String bookingNo = String.valueOf(r.getBookingNo());

                if (bookingNo.contains(newText)) {
                    filterReservationArrayList.add(r);
                }
            }

            if (filterReservationArrayList.isEmpty()) {
                ResUpcomingAdapter.setFilterList(null);
                Toast.makeText(getContext(), "No Booking No found", Toast.LENGTH_SHORT).show();

            } else {
                ResUpcomingAdapter.setFilterList(filterReservationArrayList);
            }

        }else {
            ResUpcomingAdapter.setFilterList(reservationArrayList);
        }
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
                    if(dc.getType()==DocumentChange.Type.ADDED||dc.getType()==DocumentChange.Type.MODIFIED){
                        Reservation reservation= dc.getDocument().toObject(Reservation.class);
                        try{
                            if(reservation.getStatus().equals("Accepted")){
                                reservationArrayList.add(reservation);
                            }
                        }catch(NullPointerException e){
                            Toast.makeText(getContext(), "Error with the reservation.", Toast.LENGTH_SHORT).show();

                        }

                    }

                    ResUpcomingAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())progressDialog.dismiss();
                }
            }
        });

    }
}