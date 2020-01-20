package com.ttoonic.flow;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.ttoonic.flow.Activity.Activity_Fragment_Holder;
import com.ttoonic.flow.Activity.Auth_Activity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected boolean start() {
        app_timer_to_activity(1000,3000, Auth_Activity.class).start();
        return false;
    }


}
