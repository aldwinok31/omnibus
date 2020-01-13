package com.ttoonic.flow.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ttoonic.flow.BaseActivity;
import com.ttoonic.flow.Interface.ActivityInteractive;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;
import com.ttoonic.flow.TabBaseActivity;

public class Activity_Fragment_Holder extends TabBaseActivity {
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        set_interactive();
        this.user = getIntent().getParcelableExtra("User_main");
    }

    @Override
    protected boolean start() {
        return false;
    }

    @Override
    public Fragment onFragmentInteract(Fragment fragment, Object object) {
        if(object instanceof ActivityInteractive){
            ((ActivityInteractive) object).activityCallback(this.user);
        }
        return super.onFragmentInteract(fragment,object);
    }

    @Override
    public void onBackPressed() {

    }
}
