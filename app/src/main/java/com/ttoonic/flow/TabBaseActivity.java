package com.ttoonic.flow;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ttoonic.flow.Fragment.HomeFragment;
import com.ttoonic.flow.Fragment.ReportFragment;
import com.ttoonic.flow.Fragment.HazardFragment;
import com.ttoonic.flow.Fragment.SendReportFragment;
import com.ttoonic.flow.Interface.ActivityInteractive;
import com.ttoonic.flow.Interface.FragmentInteractive;

public abstract class TabBaseActivity extends BaseActivity implements FragmentInteractive, BottomNavigationView.OnNavigationItemSelectedListener{
    private FragmentInteractive fragmentInteractive;
    private ActivityInteractive activityInteractive;
    protected BottomNavigationView navigationView;
    protected Fragment fragment;
    public void set_interactive(){
        this.fragmentInteractive = this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            fragment = getSupportFragmentManager().getFragment(savedInstanceState,"fragment");
            load_fragment(fragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);
        if(this.fragment == null){
            navigationView.setSelectedItemId(R.id.action_home);
        }

    }

    @Override
    protected boolean start() {
        return false;
    }

    @Override
    public Fragment onFragmentInteract(Fragment fragment,Object object) {
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

         fragment = null;
        switch (menuItem.getItemId()){
            case R.id.action_home:
                fragment = new HomeFragment(this);
                break;
            case R.id.action_report:
                fragment = new SendReportFragment(this);
                break;
            case R.id.action_setting:
                fragment = new HazardFragment(this);
                break;
            case R.id.action_notification:
                fragment = new ReportFragment(this);
                break;
        }

       return load_fragment(fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,"fragment",this.fragment);
    }
}
