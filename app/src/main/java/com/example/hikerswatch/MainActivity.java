package com.example.hikerswatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   LocationManager locationManager;
   LocationListener locationListener;
   TextView latitude;
   TextView longitude;
    TextView accuracy;
    TextView address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView background=(ImageView)findViewById(R.id.imageView);
        background.setImageResource(R.drawable.night);
        latitude=findViewById(R.id.latitudeView);
        longitude=findViewById(R.id.longitudeView);
        accuracy=findViewById(R.id.accuracyView);
        address=findViewById(R.id.addressView);
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        final Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(!String.valueOf(addresses.get(0).getLatitude()).equals(null))
                    latitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                    if(!String.valueOf(addresses.get(0).getLongitude()).equals(null))
                    longitude.setText(String.valueOf(addresses.get(0).getLongitude()));
                    accuracy.setText(String.valueOf(location.getAccuracy()));
                    String wholeAddress="";
                    if(addresses.get(0).getThoroughfare()!=null)
                    {
                        wholeAddress+=addresses.get(0).getThoroughfare()+" ";
                    }
                    if(addresses.get(0).getLocality()!=null)
                    {
                        wholeAddress+=addresses.get(0).getLocality()+" ";
                    }
                    if(addresses.get(0).getPostalCode()!=null) {
                        wholeAddress+=addresses.get(0).getPostalCode()+" ";
                    }
                    if(addresses.get(0).getAdminArea()!=null) {
                        wholeAddress+=addresses.get(0).getAdminArea();
                    }
                   address.setText(wholeAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation!=null)
            {
                List<Address> addresses= null;
                try {
                    addresses = geocoder.getFromLocation(lastLocation.getLatitude(),lastLocation.getLongitude(),1);
                    if(!String.valueOf(addresses.get(0).getLatitude()).equals(null))
                        latitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                    if(!String.valueOf(addresses.get(0).getLongitude()).equals(null))
                        longitude.setText(String.valueOf(addresses.get(0).getLongitude()));
                    accuracy.setText(String.valueOf(lastLocation.getAccuracy()));
                    String wholeAddress="";
                    if(addresses.get(0).getThoroughfare()!=null)
                    {
                        wholeAddress+=addresses.get(0).getThoroughfare()+" ";
                    }
                    if(addresses.get(0).getLocality()!=null)
                    {
                        wholeAddress+=addresses.get(0).getLocality()+" ";
                    }
                    if(addresses.get(0).getPostalCode()!=null) {
                        wholeAddress+=addresses.get(0).getPostalCode()+" ";
                    }
                    if(addresses.get(0).getAdminArea()!=null) {
                        wholeAddress+=addresses.get(0).getAdminArea();
                    }
                    address.setText(wholeAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            }
        }
    }