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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.CustomerLoginPage;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register_page);
        init();


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailText.getText().toString().trim();
                String password= passwordText.getText().toString().trim();
                String conPassword = confirmPasswordText.getText().toString().trim();
                String fullName= fullNameText.getText().toString().trim();
                String phoneNumber = phoneNumberText.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    emailText.setError("Email id required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordText.setError("Password required");
                    return;
                }
                if(password.length()<6){
                    passwordText.setError("Password must be longer than 6 character");
                    return;
                }
                if(!conPassword.equals(password)){
                    confirmPasswordText.setError("Different password input ");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CustomerRegisterPage.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference =fStore.collection("customers").document(userID);
                            Map<String, Object> customer= new HashMap<>();
                            customer.put("Full Name",fullName);
                            customer.put("Email", email);
                            customer.put("Phone Number", phoneNumber);
                            documentReference.set(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onSuccess: Customer profile is created for "+ userID);
                                }
                            });

                            finish();
                            startActivity(new Intent(getApplicationContext(), CustomerLoginPage.class));
                            
                        }else{
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
    }
}