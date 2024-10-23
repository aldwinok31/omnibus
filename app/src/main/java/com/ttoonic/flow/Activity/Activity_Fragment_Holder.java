package com.ttoonic.flow.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.HardwarePropertiesManager;
import android.se.omapi.SEService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Interface.ActivityInteractive;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;
import com.ttoonic.flow.Service.BatteryReciever;
import com.ttoonic.flow.Service.SMSReciever;
import com.ttoonic.flow.TabBaseActivity;


import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Activity_Fragment_Holder extends TabBaseActivity implements SensorEventListener
        , DatabaseInteractive, View.OnClickListener,BatteryReciever.onBatteryChange, OnMapReadyCallback
         ,LocationListener{
    private User user;
    private ActivityInteractive activityInteractive;
    private SensorManager sensorManager;
    private Sensor temperature;
    private DatabaseInit databaseInit;
    private ArrayList<String> stringsID;
    private BatteryReciever batteryReciever;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            fragment = getSupportFragmentManager().getFragment(savedInstanceState,"fragment");
            load_fragment(fragment);
        }
        setContentView(R.layout.activity_fragment_holder);
        set_interactive();
        this.user = getIntent().getParcelableExtra("User_main");
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.databaseInit = new DatabaseInit();
        this.databaseInit.addDatabaseSuccessListener(this);
        this.databaseInit.add_database_listener(this.user.getTeam(),this.user.getUsername());
        this.stringsID = new ArrayList<>();

        batteryReciever = new BatteryReciever(this);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(batteryReciever,intentFilter);
        buildGoogleApiClient();

    }


    @Override
    protected boolean start() {
        return false;
    }

    private Fault fault_from_fragment;
    @Override
    public Fragment onFragmentInteract(Fragment fragment, Object object) {
        if(object instanceof ActivityInteractive){
            this.activityInteractive = (ActivityInteractive) object;
            ((ActivityInteractive) object).activityCallback(this.user);
        }
        if(object instanceof Boolean){
            if((Boolean) object) {
                View view = findViewById(R.id.loading_screen);
                view.setVisibility(View.VISIBLE);
            }

            else{
                View view = findViewById(R.id.loading_screen);
                view.setVisibility(View.GONE);
            }
        }
        if (object instanceof Fault){
            navigationView.setSelectedItemId(R.id.action_location);
            fault_from_fragment = (Fault) object;
        }

        return super.onFragmentInteract(fragment,object);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(this.activityInteractive != null){
            activityInteractive.activitySensorChanged(event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(this.activityInteractive != null){
            this.activityInteractive.activityAccuracyChanged(sensor,accuracy);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,temperature,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,"fragment",this.fragment);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private AlertDialog alertDialog;

    public void showDialog(String title,String incident){

        LayoutInflater layoutInflater =this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_reports,null);

        final Button button = view.findViewById(R.id.button_safe);
        TextView textView = view.findViewById(R.id.alert_incident_dialog);
        textView.setText(incident + " DETECTED.");
        final TextView second = view.findViewById(R.id.button_second);

        button.setOnClickListener(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setView(view).setIcon(R.drawable.mysafety).setTitle(title + " Group.");
        alert.create();
        alert.setCancelable(false);
        alertDialog = alert.show();

        CountDownTimer countDownTimer = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                second.setText(Long.toString(millisUntilFinished / 1000) );
            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
                databaseInit.update_alert(user,stringsID,"marked_unsafe");
            }
        };
        countDownTimer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.batteryReciever != null){
            unregisterReceiver(this.batteryReciever);
        }


    }

    public void createNotif(String title, String incident, String desc){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(),this.getClass());
        intent.putExtra("User_main",this.user);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1003,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Report", title,
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription(desc);
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Report");


            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.mysafety)
                    .setTicker(incident)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(incident + " Alert - " + title + " Group." )
                    .setContentText(desc)
                    .setContentInfo(desc);

            notificationBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(/*notification id*/1, notificationBuilder.build());


    }

    private Fault fault;
    @Override
    public void onDatabaseSuccess(boolean data, Object object, String message) {
        if(object instanceof Fault){
            // Returns ID
            this.stringsID.add(message);

             this.fault = (Fault) object;

             if(new Date().before(fault.getExpiration())) {
                 showDialog(((Fault) object).getCategory(), ((Fault) object).getType());
                 createNotif(((Fault) object).getCategory(), ((Fault) object).getType(), ((Fault) object).getDescription());
             }

        }
        if(object instanceof  Boolean){
            this.alertDialog.dismiss();
        }
        if (object instanceof  ArrayList){
            this.fault_from_fragment = null;
            for (Fault fault : (ArrayList<Fault>) object) {
                if(this.map != null && new Date().before(fault.getExpiration())) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(fault.getLatitude(),fault.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.snippet(fault.getTitle());
                    markerOptions.title(fault.getType());
                    switch (fault.getType()){
                        case "Fire":
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            break;
                        case "Flood":
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            break;
                        case "Quake":
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            break;
                            default:
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                                break;
                    }

                   this.map.addMarker(markerOptions);
                }
            }

        }
    }

    @Override
    public void onFailure(boolean data, String message) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_safe) {
            if (this.fault != null){
                this.alertDialog.dismiss();
                this.databaseInit.update_alert(this.user,this.stringsID,"marked_safe");
            }
            this.alertDialog.dismiss();
        }

    }

    @Override
    public void onBatteryTempChange(float temperature) {
        if(this.activityInteractive != null){
          activityInteractive.activityTemperatureChange(temperature);
        }
    }


    // Google Maps
    private GoogleMap map;
    private Marker marker;
    private Location last_location;
    @Override
    public void onMapReady(GoogleMap googleMap) {
    this.map = googleMap;
    this.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
                if(this.fault_from_fragment != null){
                    create_marker_target(this.fault_from_fragment);
                }
            } else {
                //Request Location Permission
            }
        }
        else {
            map.setMyLocationEnabled(true);
        }
        this.databaseInit.get_incidents(this.user.getTeam());
    }

    private void create_marker_target(Fault fault){
        if(this.map != null ) {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(fault.getLatitude(),fault.getLongitude());
            markerOptions.position(latLng);
            markerOptions.title(fault.getType());
            markerOptions.snippet(fault.getTitle());
            switch (fault.getType()){
                case "Fire":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    break;
                case "Flood":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    break;
                case "Quake":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    break;
                default:
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    break;
            }

            this.map.addMarker(markerOptions);
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 24));
        }
    }

    protected synchronized void buildGoogleApiClient() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            this.last_location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (this.last_location != null){
                this.databaseInit.update_location(user.getUsername(),this.last_location);
            }

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,20000,0,  this);

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        this.last_location = location;
        if(this.marker !=null){
            this.marker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(this.map != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.snippet(this.user.getUsername());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            marker = this.map.addMarker(markerOptions);
            //move map camera
            if (this.fault_from_fragment == null ) {
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 24));
            }
        }

        this.databaseInit.update_location(this.user.getUsername(),this.last_location) ;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}

