package com.ttoonic.flow.Fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ttoonic.flow.Interface.ActivityInteractive;
import com.ttoonic.flow.Interface.FragmentInteractive;

public abstract class BaseFragment extends Fragment implements ActivityInteractive  {
    protected FragmentInteractive interactive;
    protected Fragment fragment;


    public BaseFragment(FragmentInteractive fragmentInteractive ) {
        this.interactive = fragmentInteractive;
    }

    @Override
    public void activityTemperatureChange(Object object) {

    }

    @Override
    public void activityCallback(Object object) {

    }

    @Override
    public void activitySensorChanged(Object object) {

    }

    @Override
    public void activityAccuracyChanged(Object object, int accuracy) {

    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        interactive.onFragmentInteract(this,this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
