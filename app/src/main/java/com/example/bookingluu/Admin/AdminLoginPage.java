package com.example.bookingluu.Admin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.Customer.Customer;
import com.example.bookingluu.Customer.CustomerProfilePage;
import com.example.bookingluu.Customer.RestaurantListPage;
import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AdminLoginPage extends AppCompatActivity {
    private Button adminLoginBtn;
    private TextView loginAsUserText;
    private EditText emailText, passwordText;
    private ProgressBar progressBar;
    FirebaseFirestore fStore;
    DocumentReference documentReference;

    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        init();

        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check validation
                String adminEmail=emailText.getText().toString().trim();
                String adminPassword= passwordText.getText().toString().trim();

                if(TextUtils.isEmpty(adminEmail)){
                    emailText.setError("Email is required!");
                    emailText.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(adminPassword)){
                    passwordText.setError("Password is required!");
                    passwordText.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(adminEmail).matches()){
                    emailText.setError("Please enter a valid email!");
                    emailText.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                adminLoginBtn.setVisibility(View.INVISIBLE);

                documentReference= fStore.collection("administrators").document(emailText.getText().toString());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String FSadminEmail = documentSnapshot.getString("email");
                            String FSadminPassw = documentSnapshot.getString("password");

                            if(emailText.getText().toString().equals(FSadminEmail)&&passwordText.getText().toString().equals(FSadminPassw)){
                                progressBar.setVisibility(View.GONE);
                                adminLoginBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(AdminLoginPage.this, "Welcome Back, "+documentSnapshot.get("restaurantName"), Toast.LENGTH_SHORT).show();

                                //finish();
                                startActivity(new Intent(getApplicationContext(), AdminMainPage.class));

                            }else{
                                progressBar.setVisibility(View.GONE);
                                adminLoginBtn.setVisibility(View.VISIBLE);
                                passwordText.setError("Wrong Password!");
                                passwordText.requestFocus();


                            }
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            adminLoginBtn.setVisibility(View.VISIBLE);
                            emailText.setError("The Admin doesn't Exists, Please Re-enter Email !");
                            emailText.requestFocus();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminLoginPage.this, "Error! Admin doesn't exist or Wrong Email or Password!", Toast.LENGTH_LONG).show();

                }
            });

            }
        });

        loginAsUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));

            }
        });
    }


    public void init(){
        adminLoginBtn=findViewById(R.id.adminLoginBtn);
        loginAsUserText=findViewById(R.id.loginAsUserText);
        emailText=findViewById(R.id.adminEmailText);
        passwordText=findViewById(R.id.adminPasswordText);
        progressBar=findViewById(R.id.progressBar);
        fStore=FirebaseFirestore.getInstance();
    }
}