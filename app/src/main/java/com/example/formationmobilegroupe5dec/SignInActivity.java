package com.example.formationmobilegroupe5dec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private TextView goToForget,goToSignUp;
    private EditText email,password;
    private Button btnSignIn;
    private String emailS, passwordS;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        goToForget = findViewById(R.id.goToForget);
        goToSignUp = findViewById(R.id.goToSignUp);
        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnSignIn.setOnClickListener(v -> {
            emailS = email.getText().toString().trim();
            passwordS = password.getText().toString().trim();
            if (!isValidEmail(emailS)) {
                email.setError("Email is invalid!!");
            } else if (passwordS.length()<5) {
                password.setError("Password is invalid!!");
            }else {
                functionSignIn(emailS,passwordS);
            }

        });

        goToForget.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this,ForgetPasswordActivity.class));
        });

        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        });
    }

    private void functionSignIn(String emailSignIn, String passwordSignIn) {
        progressDialog.setMessage("Please wait...!");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               checkEmailVerification();
           }else {
               Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show();
               progressDialog.dismiss();
           }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        if (loggedUser.isEmailVerified()){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            progressDialog.dismiss();
        }else {
            Toast.makeText(this, "Please verify your account !", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}