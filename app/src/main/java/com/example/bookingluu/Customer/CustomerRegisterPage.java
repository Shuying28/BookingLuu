package com.example.bookingluu.Customer;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.CustomerLoginPage;
import com.example.bookingluu.JavaMailAPI;
import com.example.bookingluu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegisterPage extends AppCompatActivity {
    private EditText fullNameText, phoneNumberText, emailText, passwordText, confirmPasswordText;
    private Button signUpBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private TextView haveAccountText;
    String userID;
    private ProgressBar registerProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register_page);
        init();


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerProgressBar.setVisibility(View.VISIBLE);
                signUpBtn.setVisibility(View.INVISIBLE);
                String email=emailText.getText().toString().trim();
                String password= passwordText.getText().toString().trim();
                String conPassword = confirmPasswordText.getText().toString().trim();
                String fullName= fullNameText.getText().toString().trim();
                String phoneNumber = phoneNumberText.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    emailText.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordText.setError("Password is required!");
                    return;
                }
                if(password.length()<6){
                    passwordText.setError("Password must be longer than 6 characters!");
                    return;
                }

                if(!conPassword.equals(password)){
                    confirmPasswordText.setError("Different password input!                                                                                      ");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            sendMail(fullName);
                            registerProgressBar.setVisibility(View.GONE);
                            signUpBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(CustomerRegisterPage.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID=fAuth.getCurrentUser().getUid();

                            DocumentReference documentReference =fStore.collection("customers").document(userID);
                            Customer customer= new Customer(fullName,email,phoneNumber,"");
                            documentReference.set(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onSuccess: Customer profile is created for "+ userID);
                                }
                            });

                            finish();
                            startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));
                            
                        }else{
                            registerProgressBar.setVisibility(View.GONE);
                            signUpBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(CustomerRegisterPage.this, "Error ! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        haveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));
            }
        });




    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));
    }
    private void init(){
        fullNameText=findViewById(R.id.fullNameText);
        phoneNumberText=findViewById(R.id.phoneNumberText);
        emailText=findViewById(R.id.emailText);
        passwordText=findViewById(R.id.passwordText);
        confirmPasswordText=findViewById(R.id.confirmPasswordText);
        signUpBtn=findViewById(R.id.signUpBtn);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        haveAccountText=findViewById(R.id.haveAccountText);
        registerProgressBar=findViewById(R.id.registerProgressBar);
    }


    private void sendMail(String customer) {

        String mail = emailText.getText().toString().trim();
        String message = "Dear "+customer+",\n\nThank You for choosing BookingLuu!\n\nWe are very happy to welcome you as a registered customer of BookingLuu.\nWe look forward to first reservation with us.\nIf you need any assistance please contact bookingluucustomerservice@gmail.com\n\nBest Regards,\nBookingLuu Customer Center";
        String subject = "BookingLuu User Registration";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);

        javaMailAPI.execute();

    }
}