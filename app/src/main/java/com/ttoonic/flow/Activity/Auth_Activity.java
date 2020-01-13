package com.ttoonic.flow.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ttoonic.flow.BaseActivity;
import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Pager.Pager;
import com.ttoonic.flow.R;

public class Auth_Activity extends BaseActivity implements FragmentInteractive, ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Sign-In"));
        tabLayout.addTab(tabLayout.newTab().setText("Create Account"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.view_pager);
        final Pager pager = new Pager(getSupportFragmentManager(),tabLayout.getTabCount(),this);
        viewPager.setAdapter(pager);
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        tabLayout.setScrollPosition(position,positionOffset,true);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onBackPressed() {

    }
}
