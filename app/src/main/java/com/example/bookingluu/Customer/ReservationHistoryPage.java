package com.example.bookingluu.Customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.R;
import com.example.bookingluu.Restaurant.Reservation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReservationHistoryPage extends AppCompatActivity {
    private ImageView historyBackBtn;
    private RecyclerView historyList;
    private FirebaseFirestore fStore;
    private HistoryAdapter historyAdapter;
    private TextView status;
    ArrayList<Reservation> historyArrayList;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_history_page);
        init();

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data......");
        progressDialog.show();

        historyList.setHasFixedSize(true);
        historyList.setLayoutManager(new LinearLayoutManager(ReservationHistoryPage.this));
        historyAdapter =new HistoryAdapter(ReservationHistoryPage.this, historyArrayList);

        historyList.setAdapter(historyAdapter);
        EventChangeListener();


        historyBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), CustomerProfilePage.class));
            }
        });

    }


    private void EventChangeListener() {
        fStore.collection("restaurant").document("HollandFood").collection("Reservation")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    return;
                }

                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        if (fAuth.getCurrentUser().getUid().equals(dc.getDocument().toObject(Reservation.class).getCustomerID())){
                            historyArrayList.add(dc.getDocument().toObject(Reservation.class));
                        }
                    }else if(dc.getType()==DocumentChange.Type.MODIFIED){
                        if (fAuth.getCurrentUser().getUid().equals(dc.getDocument().toObject(Reservation.class).getCustomerID())){
                            historyArrayList.set(dc.getOldIndex(),dc.getDocument().toObject(Reservation.class));
                        }
                    }

                    historyAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())progressDialog.dismiss();
                }
            }
        });

    }


    public void init(){
        historyBackBtn=findViewById(R.id.historyBackBtn);
        historyList=findViewById(R.id.historyList);
        status=findViewById(R.id.status);
        fStore= FirebaseFirestore.getInstance();
        historyArrayList= new ArrayList<>();
        fAuth=FirebaseAuth.getInstance();
    }
}