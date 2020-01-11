package com.ttoonic.flow.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ttoonic.flow.BaseActivity;
import com.ttoonic.flow.R;
import com.ttoonic.flow.TabBaseActivity;

public class Activity_Fragment_Holder extends TabBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        set_interactive();
    }

    @Override
    protected boolean start() {
        return false;
    }

    @Override
    public Fragment onFragmentInteract(Fragment fragment) {
        Toast.makeText(this, "Interacted", Toast.LENGTH_SHORT).show();
        return super.onFragmentInteract(fragment);
    }
}
