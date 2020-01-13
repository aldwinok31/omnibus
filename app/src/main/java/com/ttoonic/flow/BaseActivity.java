package com.ttoonic.flow;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


public abstract class BaseActivity extends AppCompatActivity {
     private static final int CAMERA_REQUEST_CODE = 1;
     private static final int EXTERNAL_STORAGE_CODE = 2;
     private Service service;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request_permission();
    }

    private boolean request_permission(){
        ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},EXTERNAL_STORAGE_CODE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission denied to use your Camera", Toast.LENGTH_SHORT).show();
                }
                start();
                return;
            }
        }
    }

    protected abstract boolean start();

    @Nullable
    protected CountDownTimer app_timer_to_activity(long f , long i, final Class activity){
        CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplication(),activity));
            }
        };
        return countDownTimer;
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    public Service create_service(Service service){
        return this.service = service;
    }

    @Nullable
    protected boolean load_fragment(@Nullable Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                    .replace(R.id.fragment_holder,fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
