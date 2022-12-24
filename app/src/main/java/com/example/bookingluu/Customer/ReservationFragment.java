package com.example.bookingluu.Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.Restaurant.Menu;
import com.example.bookingluu.TermsOfServicePage;
import com.example.bookingluu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ReservationFragment extends Fragment {
    private LinearLayout selectDateCon;
    private EditText dateText,nameText,phoneNoText,emailText,notesText;
    private TextView termsOfService, showSelectedFoodText;
    private CheckBox checkBox;
    private Button reserveBtn;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    private Spinner spinnerTime;

    private String[] timeslot;
    private boolean[] selectedFood;;
    ArrayList<Integer> foodPositionList;
    ArrayList<String> menuCodeList;

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
        //spinnerFood = view.findViewById(R.id.spinnerFood);
        showSelectedFoodText = view.findViewById(R.id.showSelectedFoodText);
        fStore=FirebaseFirestore.getInstance();

        //Select time
        String[] timeSlot = {"8:00am-9:00am","9:00am-10:00am","10:00am-11:00am","11:00am-12:00pm",
                "12:00pm-1:00pm","1:00pm-2:00pm","2:00pm-3:00pm","3:00pm-4:00pm",
                "4:00pm-5:00pm","5:00pm-6:00pm","6:00pm-7:00pm","7:00pm-8:00pm",
                "8:00pm-9:00pm","9:00pm-10:00pm"};

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,timeSlot);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedTimeSlot = (String) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // TODO : Here idk why cant show the menu code in multi select dropdown dialog
        //Select food
        menuCodeList = new ArrayList<>();
        foodPositionList = new ArrayList<>();
        selectedFood = new boolean[menuCodeList.size()];

        //Get menuCode from firestore
        documentReference= fStore.collection("restaurant").document("HollandFood");
        documentReference.collection("Menu").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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


        //ArrayList to Array Conversion
        String [] menuCodeArray = new String[menuCodeList.size()];

        for (int j = 0; j < menuCodeList.size(); j++) {
            menuCodeArray[j] = menuCodeList.get(j);
            System.out.println("ugiyf97td7fouygyu"+menuCodeArray[j]);
        }

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
                        String currentDateString = DateFormat.getDateInstance().format(calender.getTime());

                        dateText.setText(currentDateString);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


    }
}