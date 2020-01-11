package com.ttoonic.flow.Fragment;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ttoonic.flow.Interface.FragmentInteractive;

public class BaseFragment extends Fragment {
    protected FragmentInteractive interactive;
    public BaseFragment(FragmentInteractive fragmentInteractive) {
        this.interactive = fragmentInteractive;
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
        interactive.onFragmentInteract(this);
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
