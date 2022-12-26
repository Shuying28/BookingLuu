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

import com.example.bookingluu.Customer.MenuAdapter;
import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Menu;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AdminMenuFragment extends Fragment {

    private RecyclerView menuList;
    private FirebaseFirestore fStore;
    private AdminMenuAdapter adminMenuAdapter;
    ArrayList<Menu> menuArrayList;
    ProgressDialog progressDialog;



    public AdminMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuList=view.findViewById(R.id.menuList);
        fStore= FirebaseFirestore.getInstance();
        menuArrayList= new ArrayList<>();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data......");
        progressDialog.show();



        menuList.setHasFixedSize(true);
        menuList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        adminMenuAdapter =new AdminMenuAdapter(getActivity().getApplicationContext(),menuArrayList);

        menuList.setAdapter(adminMenuAdapter);
        EventChangeListener();


    }

    private void EventChangeListener() {
        fStore.collection("restaurant").document("HollandFood").collection("Menu").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    return;
                }
                // TODO: here cannot change the data when the menu is edited

                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        menuArrayList.add(dc.getDocument().toObject(Menu.class));
                    }else if(dc.getType()==DocumentChange.Type.MODIFIED){
                        menuArrayList.set(dc.getOldIndex(),dc.getDocument().toObject(Menu.class));

                    }
                    adminMenuAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())progressDialog.dismiss();
                }
            }
        });

    }
}