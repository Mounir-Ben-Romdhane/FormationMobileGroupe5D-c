package com.example.formationmobilegroupe5dec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private TextView goToSignIn;
    private EditText fullName,email,cin,phone,password;
    private Button btnSignUp;

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


        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
        });

        btnSignUp.setOnClickListener(v -> {
            Toast.makeText(this, "fullName : "+fullName.getText().toString()+"\n"+
                        "Email : "+email.getText().toString()+"\n"+
                            "Cin : "+cin.getText().toString()+"\n"+
                                "Phone : "+phone.getText().toString()+"\n"+
                                    "Password : "+password.getText().toString()+"\n"
                                            , Toast.LENGTH_SHORT).show();
        });

    }
}