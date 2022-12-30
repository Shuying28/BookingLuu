package com.example.bookingluu.Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.Restaurant.Menu;
import com.example.bookingluu.Restaurant.Reservation;
import com.example.bookingluu.Restaurant.Table;
import com.example.bookingluu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationFragment extends Fragment {
    private LinearLayout selectDateCon;
    private EditText dateText,nameText,phoneNoText,emailText,notesText;
    private TextView termsOfService, showSelectedFoodText, addNoPax, minusNoPax, noOfPax;
    private CheckBox checkBox;
    private Button reserveBtn;
    FirebaseFirestore fStore;
    DocumentReference menuDocumentReference, timeDocumentReference, tableDocumentReference, customerDocumentReference;
    private Spinner spinnerTime;
    private String[] timeslot;

    //For select food
    private boolean[] selectedFood;;
    ArrayList<Integer> foodPositionList;
    ArrayList<String> menuCodeList;
    String [] menuCodeArray;

    //For select assign table usage
    ArrayList<String> alTimeSlot;
    String[] timeSlot;
    int timeSlotPosition=0;
    ArrayList<Integer> suitableTable;
    Handler handler;
    ProgressDialog progressDialog;
    int selectedTableNo;
    FirebaseAuth fAuth;
    String date;


    //For making reservation
    String selectedTimeSlot;
    String reservationName;
    String reservationEmail;
    String reservationPhoneNo ;
    String reservationNotes ;
    String reservationSelectedFood ;
    String reservationSlot ;
    int reservationPax;


    //For getting the restaurant information
    String restaurantName, restaurantAddress;
    int reservationNumber;

    public ReservationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);
        termsOfService = view.findViewById(R.id.termsOfService);
        reserveBtn = view.findViewById(R.id.reserveBtn);
        selectDateCon = view.findViewById(R.id.selectDateCon);
        dateText = view.findViewById(R.id.dateText);
        spinnerTime = view.findViewById(R.id.spinnerTime);
        addNoPax=view.findViewById(R.id.addNoPax);
        minusNoPax=view.findViewById(R.id.minusNoPax);
        noOfPax= view.findViewById(R.id.noOfPax);
        //spinnerFood = view.findViewById(R.id.spinnerFood);
        showSelectedFoodText = view.findViewById(R.id.showSelectedFoodText);
        fStore=FirebaseFirestore.getInstance();
        checkBox=view.findViewById(R.id.checkBox);
        nameText=view.findViewById(R.id.nameText);
        phoneNoText=view.findViewById(R.id.phoneNoText);
        emailText=view.findViewById(R.id.emailText);
        notesText=view.findViewById(R.id.notesText);
        reserveBtn= view.findViewById(R.id.reserveBtn);
        fAuth=FirebaseAuth.getInstance();

        //initialize data
        initRestaurantData();


        //Select time
        alTimeSlot= new ArrayList<>();
        timeDocumentReference= fStore.collection("restaurant").document("HollandFood");
        timeDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                alTimeSlot = (ArrayList<String>) document.get("TimeSlot");
                timeSlot=new String[alTimeSlot.size()];
                for(int i =0;i<alTimeSlot.size();i++){
                    timeSlot[i]=alTimeSlot.get(i);
                }
                ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,timeSlot);
                timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTime.setAdapter(timeAdapter);
                spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        selectedTimeSlot = (String) adapterView.getItemAtPosition(position);
                        timeSlotPosition=position;
                        System.out.println("aidousahfpiofunweoifuweg"+position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });


        // To search for table and assign for the user
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reservationPax= Integer.parseInt(noOfPax.getText().toString());
                date = dateText.getText().toString();
                System.out.println("gjoerbgijebngjioegnjqeg"+date);
                reservationName= nameText.getText().toString();
                reservationEmail= emailText.getText().toString();
                reservationPhoneNo = phoneNoText.getText().toString();
                reservationNotes = notesText.getText().toString();
                reservationSelectedFood = showSelectedFoodText.getText().toString();
                reservationSlot = selectedTimeSlot;

                if(TextUtils.isEmpty(date)){
                    dateText.setError("Date is required! ");
                    dateText.requestFocus();
                    return;
                }else if(!TextUtils.isEmpty(date)){
                    dateText.setError(null);
                }

                if(TextUtils.isEmpty(reservationName)){
                    nameText.setError("Name is required!");
                    return;
                }
                if(TextUtils.isEmpty(reservationPhoneNo)){
                    phoneNoText.setError("Phone Number is required!");
                    return;
                }
                if(TextUtils.isEmpty(reservationEmail)){
                    emailText.setError("Email is required!");
                    return;
                }

                if(!checkBox.isChecked()){
                    Toast.makeText(getContext(), "Read the term of service before reservation", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog=new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Searching suitable table......");
                progressDialog.show();

                tableDocumentReference= fStore.collection("restaurant").document("HollandFood");
                tableDocumentReference.collection("Table").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : list) {

                                // after getting this list of document we are passing that list to our Table class.
                                Table table = documentSnapshot.toObject(Table.class);
                                int tableNoPax= table.getPax();
                                int tableNo= table.getTableNo();
                                suitableTable=new ArrayList<>();

                                System.out.println("ihyvuofufiyoyvoiyv80y"+tableNoPax);
                                if(tableNoPax>=reservationPax&&reservationPax+1>=tableNoPax){
                                    tableDocumentReference.collection("Table").document(String.valueOf(tableNo))
                                            .collection("Date").document(date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        StringBuilder availability = new StringBuilder(documentSnapshot.getString("TimeSlot"));
                                                        System.out.println(availability.toString());
                                                        if(availability.charAt(timeSlotPosition)=='0'){
                                                            System.out.println("ihyvuofufiyoyvoiyv80yj vviyub"+tableNo);
                                                            suitableTable.add(tableNo);


                                                        }
                                                    }else{
                                                        StringBuilder newAvailability = new StringBuilder(newDateString(alTimeSlot.size()));
                                                        Map<String, String> time= new HashMap<String, String>() ;
                                                        time.put("TimeSlot", newAvailability.toString());
                                                        tableDocumentReference.collection("Table").document(String.valueOf(tableNo)).collection("Date")
                                                                .document(date).set(time);
                                                        suitableTable.add(tableNo);



                                                    }

                                                }
                                            });
                                }
                            }


                        }else {
                            Toast.makeText(getContext(), "No Table Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: set up the data in the dialog
                        progressDialog.dismiss();
                        if(suitableTable.size()==0){
                            Toast.makeText(getContext(), "Time slot is full. Try another slot ", Toast.LENGTH_SHORT).show();
                        }else{
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                            bottomSheetDialog.setContentView(R.layout.customer_reservation_info);
                            bottomSheetDialog.show();
                            ImageView reservationProfilePic = bottomSheetDialog.findViewById(R.id.profilePic);
                            TextView resNameText=bottomSheetDialog.findViewById(R.id.resNameText);
                            TextView resEmail= bottomSheetDialog.findViewById(R.id.resEmail);
                            TextView resPhoneNo = bottomSheetDialog.findViewById(R.id.resPhoneNo);
                            TextView resRestaurantName= bottomSheetDialog.findViewById(R.id.resRestaurantName);
                            TextView resRestaurantAddress= bottomSheetDialog.findViewById(R.id.resRestaurantAddress);
                            TextView resDate= bottomSheetDialog.findViewById(R.id.resDate);
                            TextView resTimeSlot= bottomSheetDialog.findViewById(R.id.resTimeSlot);
                            TextView resPax= bottomSheetDialog.findViewById(R.id.resPax);
                            TextView resSelectedMenu= bottomSheetDialog.findViewById(R.id.resSelectedMenu);
                            TextView resTableNo= bottomSheetDialog.findViewById(R.id.resTableNo);
                            TextView resNote= bottomSheetDialog.findViewById(R.id.resNote);
                            ImageView closeBtn= bottomSheetDialog.findViewById(R.id.closeBtn);
                            Button confirmReservationBtn = bottomSheetDialog.findViewById(R.id.confirmReservationBtn);






                            // ASSIGN THE TABLE as the first table in the list
                            selectedTableNo=suitableTable.get(0);
                            customerDocumentReference= fStore.collection("customers").document(fAuth.getUid());
                            customerDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                    String imageURI =value.getString("image");
                                    Picasso.get().load(imageURI).into(reservationProfilePic);

                                }
                            });

                            resNameText.setText(reservationName);;
                            resEmail.setText(reservationEmail);
                            resPhoneNo.setText(reservationPhoneNo);
                            resRestaurantName.setText(restaurantName);
                            resRestaurantAddress.setText(restaurantAddress);
                            resDate.setText(date);
                            resTimeSlot.setText(reservationSlot);
                            resPax.setText(reservationPax+" people");
                            resSelectedMenu.setText(reservationSelectedFood);
                            resTableNo.setText("Table No " + String.valueOf(selectedTableNo));
                            resNote.setText(reservationNotes);


                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bottomSheetDialog.dismiss();
                                }
                            });


                            //make reservation to database
                            confirmReservationBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Reservation reservation = new Reservation(reservationNumber,reservationPax,selectedTableNo,date,reservationSlot,
                                            reservationSelectedFood,fAuth.getUid(),reservationName,reservationPhoneNo,reservationEmail,reservationNotes,restaurantName);
                                    DocumentReference reservationDocumentReference =fStore.collection("restaurant").document("HollandFood").collection("Reservation").document(String.valueOf(reservationNumber));
                                    reservationDocumentReference.set(reservation).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //Update data to firestore
                                            updateRestaurantData();
                                            updateTableAvailability(restaurantName,selectedTableNo,timeSlotPosition);
                                            bottomSheetDialog.dismiss();
                                            Toast.makeText(getContext(), "Reservation created. Pending approval", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                }
                            });


                        }





                    }
                },1500);






            }
        });



        //Select food
        menuCodeList = new ArrayList<>();
        foodPositionList = new ArrayList<>();

        //Get menuCode from firestore
        menuDocumentReference= fStore.collection("restaurant").document("HollandFood");
        menuDocumentReference.collection("Menu").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : list) {
                                // after getting this list of document we are passing that list to our Menu class.
                                Menu menu = documentSnapshot.toObject(Menu.class);
                                String menuCode = menu.getMenuCode();
                                String menuName = menu.getMenuName();
                                menuCodeList.add(menuCode+" "+menuName);
                            }
                            arrayListToArray();
                            selectedFood = new boolean[menuCodeList.size()];

                        }else {
                            Toast.makeText(getContext(), "No Food Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                });



        //Multi select drop down
        showSelectedFoodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Food");
                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(menuCodeArray, selectedFood, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            // when checkbox selected, add position  in foodposition list
                            foodPositionList.add(i);
                            //Sort array list
                            Collections.sort(foodPositionList);
                        } else {
                            // when checkbox unselected, remove position from foodposition list
                            for(int j=0;j< foodPositionList.size();j++){
                                if(foodPositionList.get(j)==i){
                                    foodPositionList.remove(i);
                                }
                            }
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int j = 0; j < foodPositionList.size(); j++) {
                            // concat array value
                            stringBuilder.append(menuCodeArray[foodPositionList.get(j)]);
                            if (j != foodPositionList.size() - 1) {
                                // When j value  not equal to foodPosition list size - 1
                                stringBuilder.append(", ");
                            }
                        }
                        showSelectedFoodText.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectedFood.length; j++) {
                            // remove all selection
                            selectedFood[j] = false;
                            // clear foodPosition list
                            foodPositionList.clear();
                            // clear text view value
                            showSelectedFoodText.setText("");
                        }
                    }
                });

                builder.show();
            }
        });


        //Select Date
        //Use the current date as the default date in the picker
        final Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        selectDateCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        calender.set(Calendar.YEAR,year);
                        calender.set(Calendar.MONTH,month);
                        calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String currentDateString = dayOfMonth+"-"+month+"-"+year;

                        dateText.setText(currentDateString);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        addNoPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(noOfPax.getText().toString())>=1){
                    noOfPax.setText(String.valueOf(Integer.parseInt(noOfPax.getText().toString())+1));
                }

            }
        });

        minusNoPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(noOfPax.getText().toString())>=2){
                    noOfPax.setText(String.valueOf(Integer.parseInt(noOfPax.getText().toString())-1));
                }

            }
        });






    }
    public void arrayListToArray(){
        menuCodeArray = new String[menuCodeList.size()];

        for (int j = 0; j < menuCodeList.size(); j++) {
            menuCodeArray[j] = menuCodeList.get(j);
        }
    }

    public String newDateString(int n){
        String res="";
        for(int i=0;i<n;i++){
            res+="0";
        }
        return res;
    }

    public void initRestaurantData(){
        DocumentReference restaurantDocumentReference =fStore.collection("restaurant").document("HollandFood");
        restaurantDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                restaurantName =value.getString("RestaurantName");
                restaurantAddress =value.getString("Address");
                reservationNumber = (int)(long)value.getLong("ReservationNumber");

            }
        });
    }

    public void updateRestaurantData(){
        DocumentReference restaurantDocumentReference =fStore.collection("restaurant").document("HollandFood");
        restaurantDocumentReference.update("ReservationNumber", reservationNumber+1);
    }

    public void updateTableAvailability(String restaurantName, int tableNo, int timeSlot){
        DocumentReference dateDocumentReference =fStore.collection("restaurant").document(restaurantName).collection("Table")
                .document(String.valueOf(tableNo)).collection("Date").document(date);
        dateDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                StringBuilder availability = new StringBuilder(value.getString("TimeSlot"));
                availability.setCharAt(timeSlot,'1');
                System.out.println(availability);
                dateDocumentReference.update("TimeSlot",availability.toString());

            }
        });
    }
}