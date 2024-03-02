package com.example.formationmobilegroupe5dec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DevicesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;
    private EditText deviceName, deviceValue;
    private Button addDevice;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String name, value;

    private ListView listDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        drawerLayout = findViewById(R.id.drawer_home);
        navigationView = findViewById(R.id.navigation_home);
        iconMenu = findViewById(R.id.icon_home);
        deviceName = findViewById(R.id.etDeviceName);
        deviceValue = findViewById(R.id.etDeviceValue);
        addDevice = findViewById(R.id.btnAddDevice);
        listDevices = findViewById(R.id.listDevices);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        ArrayList<String> deviceArrayList = new ArrayList<>();
        ArrayAdapter<String> devicesAdapter = new ArrayAdapter<>(this, R.layout.list_item, deviceArrayList);
        listDevices.setAdapter(devicesAdapter);

        DatabaseReference devicesRef = firebaseDatabase.getReference().child("Devices");
        
        devicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deviceArrayList.clear();
                for (DataSnapshot deviceSnapshot :
                        snapshot.getChildren()) {
                    deviceArrayList.add(deviceSnapshot.child("name").getValue().toString() + " : " + deviceSnapshot.child("value").getValue().toString());
                }
                devicesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DevicesActivity.this, "Error !", Toast.LENGTH_SHORT).show();
            }
        });


        addDevice.setOnClickListener(v -> {
            name = deviceName.getText().toString().trim();
            value = deviceValue.getText().toString().trim();
            if (name.isEmpty()) {
                deviceName.setError("Required");
            } else if (value.isEmpty()) {
                deviceValue.setText("Required");
            }else{
                HashMap<String, String > deviceMap = new HashMap<>();
                deviceMap.put("name", name);
                deviceMap.put("value", value);
                myRef.child("Devices").push().setValue(deviceMap);

                deviceName.setText("");
                deviceValue.setText("");
                deviceValue.clearFocus();
                deviceName.clearFocus();
                Toast.makeText(this, "New device added successfully !", Toast.LENGTH_SHORT).show();
            }
        });

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.devices:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.profile:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DevicesActivity.this, ProfilActivity.class));
                    break;
            }
            return true;
        });

    }

    private void navigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.devices);
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