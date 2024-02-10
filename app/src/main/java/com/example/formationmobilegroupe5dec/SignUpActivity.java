package com.example.formationmobilegroupe5dec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formationmobilegroupe5dec.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private TextView goToSignIn;
    private EditText fullName, email, cin, phone, password;
    private String fullNameS, emailS, cinS, phoneS, passwordS;
    private Button btnSignUp;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        goToSignIn = findViewById(R.id.goToSignIn);
        fullName = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        cin = findViewById(R.id.userCin);
        phone = findViewById(R.id.userNumber);
        password = findViewById(R.id.userPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();


        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        btnSignUp.setOnClickListener(v -> {

            if (validate()) {
                progressDialog.setMessage("Please wait...!!");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailS.trim(), passwordS.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                        }else {
                            Toast.makeText(SignUpActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

    }

    private void sendEmailVerification() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth != null ) {
            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserDate();
                        Toast.makeText(SignUpActivity.this, "Registration done ! Please verifie you email account !", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    }else {
                        Toast.makeText(SignUpActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void sendUserDate() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference("Users");
        User newUser = new User(fullNameS, emailS, phoneS, cinS);
        myReference.child(""+firebaseAuth.getUid()).setValue(newUser);
    }

    private boolean validate() {

        boolean res = false;
        fullNameS = fullName.getText().toString();
        emailS = email.getText().toString();
        cinS = cin.getText().toString();
        phoneS = phone.getText().toString();
        passwordS = password.getText().toString();

        if (fullNameS.isEmpty() || fullNameS.length() < 7) {
            fullName.setError("Name is invalid!!");
        } else if (!isValidEmail(emailS)) {
            email.setError("Email is invalid!!");
        } else if (phoneS.isEmpty() || phoneS.length() != 8) {
            phone.setError("Phone is invalid!");
        } else if (cinS.isEmpty() || cinS.length() != 8) {
            cin.setError("CIN is invalid!");
        } else if (passwordS.isEmpty() || passwordS.length() < 5) {
            password.setError("Password is invalid!");
        } else {
            res = true;
        }

        return res;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}