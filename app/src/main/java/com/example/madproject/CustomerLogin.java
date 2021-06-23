package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLogin extends AppCompatActivity {

    Button btnCustomerBack, btnCustomerLogin, btnCustomerRegister;
    TextView tvCustomerStatus, tvCustomerRegister;
    EditText etCustomerEmail, etCustomerPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference CustomerDatabaseRef;
    private String onlineCustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if(currentUser != null) {
                    Intent intent = new Intent(CustomerLogin.this, CustomersMapActivity.class);
                    startActivity(intent);
                }
            }
        };



        btnCustomerBack = findViewById(R.id.btnCustomerBack);
        btnCustomerLogin = findViewById(R.id.btnCustomerLogin);
        btnCustomerRegister = findViewById(R.id.btnCustomerRegister);
        tvCustomerStatus = findViewById(R.id.tvCustomerStatus);
        tvCustomerRegister = findViewById(R.id.tvCustomerRegister);
        etCustomerEmail = findViewById(R.id.etCustomerEmail);
        etCustomerPassword = findViewById(R.id.etCustomerPassword);

        btnCustomerRegister.setVisibility(View.INVISIBLE);
        btnCustomerRegister.setEnabled(false);

        btnCustomerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLogin.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        tvCustomerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCustomerLogin.setVisibility(View.INVISIBLE);
                tvCustomerRegister.setVisibility(View.INVISIBLE);

                btnCustomerRegister.setVisibility(View.VISIBLE);
                btnCustomerRegister.setEnabled(true);
            }
        });

        btnCustomerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etCustomerEmail.getText().toString();
                String password = etCustomerPassword.getText().toString();

                RegisterCustomer(email, password);
            }
        });
        
        btnCustomerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etCustomerEmail.getText().toString();
                String password = etCustomerPassword.getText().toString();
                
                LoginCustomer(email, password);
            }
        });
    }

    private void LoginCustomer(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(CustomerLogin.this, "Email is mandatory", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(CustomerLogin.this, "Password is mandatory", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(CustomerLogin.this, CustomersMapActivity.class);
                                startActivity(intent);

                                Toast.makeText(CustomerLogin.this, "Customer Login Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CustomerLogin.this, "Unsuccessful Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void RegisterCustomer(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(CustomerLogin.this, "Email is mandatory", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(CustomerLogin.this, "Password is mandatory", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                onlineCustomerId = mAuth.getCurrentUser().getUid();
                                CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(onlineCustomerId);
                                CustomerDatabaseRef.setValue(true);

                                Intent intent = new Intent(CustomerLogin.this, CustomersMapActivity.class);
                                startActivity(intent);

                                Toast.makeText(CustomerLogin.this, "Customer Registration Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CustomerLogin.this, "Unsuccessful Registration", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}