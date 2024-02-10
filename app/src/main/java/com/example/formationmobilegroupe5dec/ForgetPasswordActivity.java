package com.example.formationmobilegroupe5dec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Button goToSignIn, btnForgetPass;
    private EditText email;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String emailS;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btnForgetPass = findViewById(R.id.btnResetPass);
        email = findViewById(R.id.emailForget);
        goToSignIn = findViewById(R.id.btnBackToSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(ForgetPasswordActivity.this,SignInActivity.class));
        });

        btnForgetPass.setOnClickListener(v -> {
            emailS = email.getText().toString().trim();
            if (!isValidEmail(emailS)) {
                email.setError("Email is invalid");
            }else {
                progressDialog.setMessage("Please wait...!");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(emailS).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email sent ! check your email address !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetPasswordActivity.this, SignInActivity.class));
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(this, "Password reset email failed !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}