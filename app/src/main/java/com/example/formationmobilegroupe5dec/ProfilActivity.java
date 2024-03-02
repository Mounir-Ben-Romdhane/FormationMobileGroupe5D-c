package com.example.formationmobilegroupe5dec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText fullName, email, cin, phone;
    private Button btnEdit, btnLogOut;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser logedUser;
    private DatabaseReference userReference;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        fullName = findViewById(R.id.userNameProfil);
        email = findViewById(R.id.userEmailProfil);
        cin = findViewById(R.id.userCinProfil);
        phone = findViewById(R.id.userNumberProfil);
        btnEdit = findViewById(R.id.btnEditProfil);
        btnLogOut = findViewById(R.id.btnLogOut);

        drawerLayout = findViewById(R.id.drawer_profile);
        navigationView = findViewById(R.id.navigation_profile);
        iconMenu = findViewById(R.id.icon_profile);

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.devices:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(ProfilActivity.this, DevicesActivity.class));
                    break;
                case R.id.profile:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        logedUser = firebaseAuth.getCurrentUser();
        try {
            userReference = firebaseDatabase.getReference().child("Users").child(logedUser.getUid());

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fullName.setText(snapshot.child("fullName").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    cin.setText(snapshot.child("cin").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfilActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            System.out.println(e);
        }
        
        btnLogOut.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("remember", false);
            editor.apply();
            startActivity(new Intent(ProfilActivity.this, SignInActivity.class));
            Toast.makeText(this, "Log out successfully!!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            userReference.child("fullName").setValue(fullName.getText().toString().trim());
            userReference.child("cin").setValue(cin.getText().toString().trim());
            userReference.child("phone").setValue(phone.getText().toString().trim());
            Toast.makeText(this, "Your data has been changed successfully!!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        });

    }

    private void navigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.profile);
        navigationView.bringToFront();

        iconMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else  drawerLayout.openDrawer(GravityCompat.START);
        });

        drawerLayout.setScrimColor(getResources().getColor(R.color.colorApp));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

}