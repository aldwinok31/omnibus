package com.ttoonic.flow.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ttoonic.flow.BaseActivity;
import com.ttoonic.flow.Interface.ActivityInteractive;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;
import com.ttoonic.flow.TabBaseActivity;

import java.io.ByteArrayOutputStream;

public class Activity_Fragment_Holder extends TabBaseActivity implements SensorEventListener {
    private User user;
    private ActivityInteractive activityInteractive;
    private SensorManager sensorManager;
    private Sensor temperature;
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
        return super.onFragmentInteract(fragment,object);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1 ) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
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
}

