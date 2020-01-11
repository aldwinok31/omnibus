package com.ttoonic.flow;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ttoonic.flow.Fragment.HomeFragment;
import com.ttoonic.flow.Fragment.ReportFragment;
import com.ttoonic.flow.Fragment.SettingFragment;
import com.ttoonic.flow.Interface.FragmentInteractive;

public abstract class TabBaseActivity extends BaseActivity implements FragmentInteractive, BottomNavigationView.OnNavigationItemSelectedListener{
    private FragmentInteractive fragmentInteractive;
    public void set_interactive(){
        this.fragmentInteractive = this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.action_home);

    }

    @Override
    protected boolean start() {
        return false;
    }

    @Override
    public Fragment onFragmentInteract(Fragment fragment) {
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.action_home:
                fragment = new HomeFragment(this);
                break;
            case R.id.action_report:
                fragment = new ReportFragment(this);
                break;
            case R.id.action_setting:
                fragment = new SettingFragment(this);
                break;
        }

       return load_fragment(fragment);
    }
}
