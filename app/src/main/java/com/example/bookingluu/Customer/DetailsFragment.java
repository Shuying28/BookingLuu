package com.example.bookingluu.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.SplashActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.example.bookingluu.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class DetailsFragment extends Fragment {
    private GoogleMap gMap;
    final String CURRENT_RESTAURANT= RestaurantListPage.passString;
    private TextView detailResAddress, detailResPhone, detailResOperateHour, detailResCuisine, detailResPaymentOption;
    FirebaseFirestore firebaseFirestore;
    private String longitude, latitude;


    public DetailsFragment() {
        // Required empty public constructor
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * We just add a marker near Kolej Kediaman Kinabalu.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;

            //More Settings
            gMap.getUiSettings().setCompassEnabled(true);
            gMap.getUiSettings().setZoomGesturesEnabled(true);
            gMap.getUiSettings().setZoomControlsEnabled(true);

            //Add a marker in HollandFond Restaurant and move the camera
            try {
                LatLng restaurantLoc = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude) );
                gMap.addMarker(new MarkerOptions().position(restaurantLoc).title(CURRENT_RESTAURANT));
                moveToCurrentLocation(restaurantLoc);
            }catch (NullPointerException e){

            }


        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailResAddress= view.findViewById(R.id.detailResAddress);
        detailResPhone=view.findViewById(R.id.detailResPhone);
        detailResOperateHour=view.findViewById(R.id.detailResOperateHour);
        detailResCuisine=view.findViewById(R.id.detailResCuisine);
        detailResPaymentOption=view.findViewById(R.id.detailResPaymentOption);
        firebaseFirestore=FirebaseFirestore.getInstance();
        setUpData();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

    public void setUpData(){
        DocumentReference documentReference= firebaseFirestore.collection("restaurant").document(CURRENT_RESTAURANT);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                detailResAddress.setText(value.getString("Address"));
                detailResPhone.setText(value.getString("PhoneNo"));
                detailResOperateHour.setText(value.getString("OperateHour"));
                detailResCuisine.setText(value.getString("Cuisine"));
                detailResPaymentOption.setText(value.getString("PaymentOption"));
                longitude=value.getString("Longitude");
                latitude=value.getString("Latitude");

            }
        });

    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        gMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15),  null);


    }

}