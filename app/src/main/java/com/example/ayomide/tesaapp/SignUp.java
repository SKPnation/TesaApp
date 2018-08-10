package com.example.ayomide.tesaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ayomide.tesaapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    TextInputLayout etPhone, etEmail, etPassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getEditText().getText().toString().trim();
                String phone = etPhone.getEditText().getText().toString().trim();
                String password = etPassword.getEditText().getText().toString().trim();

                if (!validateInputs(phone, email, password)) {

                    User user = new User(
                            email,
                            password
                    );
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Calm down...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // check if already user phone
                            if (dataSnapshot.child(etPhone.getEditText().getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone already registered", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                mDialog.dismiss();
                                User user = new User(etEmail.getEditText().getText().toString(),
                                        etPassword.getEditText().getText().toString());
                                table_user.child(etPhone.getEditText().getText().toString()).setValue(user);

                                Intent homeintent = new Intent(SignUp.this, Home.class);
                                startActivity(homeintent);
                                Toast.makeText(SignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
    private boolean validateInputs(String phone, String email, String password) {
        if (phone.isEmpty()) {
            etPhone.setError("phone required");
            etPhone.requestFocus();
            return true;
        }
        if (phone.length() != 11) {
            etPhone.setError("Invalid number");
            etPhone.requestFocus();
            return true;
        }

        if (email.isEmpty()) {
            etEmail.setError("email required");
            etEmail.requestFocus();
            return true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            etEmail.requestFocus();
            return true;
        }


        if (password.isEmpty()) {
            etPassword.setError("password required");
            etPassword.requestFocus();
            return true;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be more than six characters");
            etPassword.requestFocus();
            return true;
        }

        return false;
    }
}
