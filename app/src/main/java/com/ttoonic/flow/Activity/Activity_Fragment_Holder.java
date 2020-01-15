package com.ttoonic.flow.Activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.HardwarePropertiesManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Interface.ActivityInteractive;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;
import com.ttoonic.flow.Service.BatteryReciever;
import com.ttoonic.flow.TabBaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Activity_Fragment_Holder extends TabBaseActivity implements SensorEventListener
        , DatabaseInteractive, View.OnClickListener,BatteryReciever.onBatteryChange {
    private User user;
    private ActivityInteractive activityInteractive;
    private SensorManager sensorManager;
    private Sensor temperature;
    private DatabaseInit databaseInit;
    private ArrayList<String> stringsID;

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

        BatteryReciever batteryReciever = new BatteryReciever(this);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(batteryReciever,intentFilter);
    }


    @Override
    protected boolean start() {
        return false;
    }

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
        View view = layoutInflater.inflate(R.layout.dialog_reports,null);

        Button button = view.findViewById(R.id.button_safe);
        Button button2 = view.findViewById(R.id.button_unsafe);
        TextView textView = view.findViewById(R.id.alert_incident_dialog);
        textView.setText(incident + " DETECTED.");

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setView(view).setIcon(R.drawable.mysafety).setTitle(title + " Group.");
        alert.create();
        alertDialog = alert.show();

    }

    public void createNotif(String title,String incident,String desc){
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

             showDialog(((Fault) object).getCategory(),((Fault) object).getType());
             createNotif(((Fault) object).getCategory(),((Fault) object).getType(),((Fault) object).getDescription());

        }
        if(object instanceof  Boolean){
            this.alertDialog.dismiss();
        }
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
        if(v.getId() == R.id.button_unsafe) {
            if (this.fault != null){
                this.alertDialog.dismiss();
                this.databaseInit.update_alert(this.user,this.stringsID,"marked_unsafe");
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
}

