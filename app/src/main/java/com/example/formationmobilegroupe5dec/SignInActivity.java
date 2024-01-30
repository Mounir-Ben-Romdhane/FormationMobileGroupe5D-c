package com.example.formationmobilegroupe5dec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private TextView goToForget,goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        goToForget = findViewById(R.id.goToForget);
        goToSignUp = findViewById(R.id.goToSignUp);

        goToForget.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this,ForgetPasswordActivity.class));
        });

        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        });
    }
}