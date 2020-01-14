package com.ttoonic.flow.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;


public class ReportFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    public ReportFragment(FragmentInteractive fragmentInteractive) {
        super(fragmentInteractive);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.view = inflater.inflate(R.layout.fragment_report_list,null);
    }
    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;


    }
    @Override
    public void activityCallback(Object object) {
        super.activityCallback(object);
        if(object instanceof User){
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
