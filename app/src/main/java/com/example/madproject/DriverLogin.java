
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLogin extends AppCompatActivity {

    Button btnDriverBack, btnDriverLogin, btnDriverRegister;
    TextView tvDriverStatus, tvDriverRegister;
    EditText etDriverEmail, etDriverPassword;

    private  FirebaseAuth mAuth;
    private DatabaseReference DriverDatabaseRef;
    private String onlineDriverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        btnDriverBack = findViewById(R.id.btnDriverBack);
        btnDriverLogin = findViewById(R.id.btnDriverLogin);
        btnDriverRegister = findViewById(R.id.btnDriverRegister);
        tvDriverStatus = findViewById(R.id.tvDriverStatus);
        tvDriverRegister = findViewById(R.id.tvDriverRegister);
        etDriverEmail = findViewById(R.id.etDriverEmail);
        etDriverPassword = findViewById(R.id.etDriverPassword);


        btnDriverRegister.setVisibility(View.INVISIBLE);
        btnDriverRegister.setEnabled(false);

        btnDriverBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverLogin.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        tvDriverRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDriverLogin.setVisibility(View.INVISIBLE);
                tvDriverRegister.setVisibility(View.INVISIBLE);

                btnDriverRegister.setVisibility(View.VISIBLE);
                btnDriverRegister.setEnabled(true);
            }
        });

        btnDriverRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etDriverEmail.getText().toString();
                String password = etDriverPassword.getText().toString();

                RegisterDriver(email, password);
            }
        });
        
        btnDriverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etDriverEmail.getText().toString();
                String password = etDriverPassword.getText().toString();
                
                LoginDriver(email, password);
            }
        });
    }

    private void LoginDriver(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(DriverLogin.this, "Email is mandatory", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(DriverLogin.this, "Password is mandatory", Toast.LENGTH_SHORT).show();
        }

        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                Intent intent = new Intent(DriverLogin.this, DriversMapActivity.class);
                                startActivity(intent);

                                Toast.makeText(DriverLogin.this, "Driver Login Successful", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(DriverLogin.this, "Unsuccessful Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void RegisterDriver(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(DriverLogin.this, "Email is mandatory", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(DriverLogin.this, "Password is mandatory", Toast.LENGTH_SHORT).show();
        }

        else {
             mAuth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    onlineDriverId = mAuth.getCurrentUser().getUid();
                                    DriverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(onlineDriverId);
                                    DriverDatabaseRef.setValue(true);

                                    Intent intent = new Intent(DriverLogin.this, DriversMapActivity.class);
                                    startActivity(intent);

                                    Toast.makeText(DriverLogin.this, "Driver Registration Successful", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(DriverLogin.this, "Unsuccessful Registration", Toast.LENGTH_SHORT).show();
                                }
                         }
                     });
        }
    }
}