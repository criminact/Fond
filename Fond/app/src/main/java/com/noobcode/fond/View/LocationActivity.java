package com.noobcode.fond.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.noobcode.fond.R;
import com.noobcode.fond.ViewModel.AskingRuntimePermissions;
import com.noobcode.fond.ViewModel.FusedLocations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {

    AskingRuntimePermissions askingRuntimePermissions;

    public String address;
    private double latitude = 0.0, longitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isContinue = false;
    private boolean isGPS = false;
    FusedLocations fusedLocations;
    TextView addresslocation;
    Button go;
    Button locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        addresslocation = (TextView) findViewById(R.id.addressLocation);
        go = (Button) findViewById(R.id.nextfour);
        locationButton = (Button) findViewById(R.id.locationButton);


        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLocationMapper();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, PhotoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("age", getIntent().getStringExtra("age"));
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("gender", getIntent().getStringExtra("gender"));
                intent.putExtra("facebookdp", getIntent().getStringExtra("facebookdp"));
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("lookingfor", getIntent().getStringExtra("lookingFor"));
                intent.putExtra("matchwith", getIntent().getStringExtra("matchWith"));
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("userId", getIntent().getStringExtra("userId"));
                intent.putExtra("address", address);
                startActivity(intent);

            }
        });

    }

    private void callLocationMapper() {
        askingRuntimePermissions = new AskingRuntimePermissions(this);

        if (askingRuntimePermissions.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && askingRuntimePermissions.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLocation();
        } else {
            //permission denied
            //ask for permissions
            askingRuntimePermissions.askPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }


        fusedLocations = new FusedLocations(LocationActivity.this, this);
        fusedLocations.initializeFusedLocations();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        fusedLocations.turnGPSOn(new FusedLocations.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;
                Toast.makeText(LocationActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        if (!isContinue) {
                            geocoder();
                        } else {
                            geocoder();
                        }
                        if (!isContinue && fusedLocations.getmFusedLocationClient() != null) {
                            fusedLocations.getmFusedLocationClient().removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        if (!isGPS) {
            Toast.makeText(this, "Please turn on the GPS.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            isContinue = false;
            getLocation();
        }

    }

    private void getLocation() {
        if (isGPS) {
            if (isContinue) {
                fusedLocations.getmFusedLocationClient().requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                fusedLocations.getmFusedLocationClient().getLastLocation().addOnSuccessListener(LocationActivity.this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        geocoder();
                    } else {
                        fusedLocations.getmFusedLocationClient().requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
            }
        } else {
            Toast.makeText(this, "GPS is turned off, Turn on to use this feature", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        fusedLocations.getmFusedLocationClient().requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        fusedLocations.getmFusedLocationClient().getLastLocation().addOnSuccessListener(LocationActivity.this, location -> {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                geocoder();
                            } else {
                                fusedLocations.getmFusedLocationClient().requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Permission(s) denied", Toast.LENGTH_SHORT).show();
                    askingRuntimePermissions.askPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                }
                break;
            }
        }
    }

    private void geocoder() {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            address = (addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality());
            addresslocation.setText(address);
            go.setVisibility(View.VISIBLE);
        }
    }
}