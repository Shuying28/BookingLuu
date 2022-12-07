package com.example.bookingluu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingluu.Customer.CustomerRegisterPage;
import com.example.bookingluu.Customer.ForgotPasswordPage;
import com.example.bookingluu.Customer.RestaurantListPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerLoginPage extends AppCompatActivity {
    private TextView registerText,forgotPasswordText;
    private EditText emailText, passwordText;
    private Button loginBtn;
    FirebaseAuth fAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_page);
        init();


        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CustomerLoginPage.this, CustomerRegisterPage.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CustomerLoginPage.this, ForgotPasswordPage.class);
                finish();
                startActivity(intent);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailText.getText().toString().trim();
                String password= passwordText.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailText.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordText.setError("Password is required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.INVISIBLE);

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(CustomerLoginPage.this, "Login Successful ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), RestaurantListPage.class));
                            finish();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(CustomerLoginPage.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }

    public void init(){
        registerText=findViewById(R.id.registerText);
        forgotPasswordText=findViewById(R.id.forgotPasswordText);
        emailText=findViewById(R.id.emailText);
        passwordText=findViewById(R.id.passwordText);
        loginBtn=findViewById(R.id.loginBtn);
        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
    }

}