package com.example.bookingluu.Customer;

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
import android.widget.ProgressBar;

import com.example.bookingluu.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RatingFragment extends Fragment {
    private RecyclerView ratingList;
    private FirebaseFirestore fStore;
    private RatingAdapter ratingAdapter;
    ArrayList<Rating> ratingArrayList;
    ProgressDialog progressDialog;




    public RatingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ratingList=view.findViewById(R.id.ratingList);
        fStore= FirebaseFirestore.getInstance();
        ratingArrayList= new ArrayList<>();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data......");
        progressDialog.show();



        ratingList.setHasFixedSize(true);
        ratingList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        ratingAdapter =new RatingAdapter(getActivity().getApplicationContext(),ratingArrayList);

        ratingList.setAdapter(ratingAdapter);
        EventChangeListener();


    }

    private void EventChangeListener() {
        fStore.collection("restaurant").document("HollandFood").collection("RatingList").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        ratingArrayList.add(dc.getDocument().toObject(Rating.class));
                    }
                    ratingAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())progressDialog.dismiss();
                }
            }
        });

    }
}