package com.ttoonic.flow.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.R;

public class HomeFragment extends BaseFragment {


    public HomeFragment(FragmentInteractive interactive) {
       super(interactive);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOAD", "onCreateView: ");
        return inflater.inflate(R.layout.fragment_home,null);
    }
}
