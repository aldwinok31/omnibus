package com.ttoonic.flow.Pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ttoonic.flow.Fragment.LoginFragment;
import com.ttoonic.flow.Fragment.RegisterFragment;
import com.ttoonic.flow.Interface.FragmentInteractive;

public class Pager extends FragmentStatePagerAdapter {

    private int tab_number;
    private FragmentInteractive fragmentInteractive;

    public Pager(FragmentManager fm, int num, FragmentInteractive fragmentInteractive) {
        super(fm);
        this.tab_number = num;
        this.fragmentInteractive = fragmentInteractive;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LoginFragment fragment = new LoginFragment(this.fragmentInteractive);
                return fragment;
            case 1:
                RegisterFragment fragment1 = new RegisterFragment(this.fragmentInteractive);
                return fragment1;

                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return this.tab_number;
    }
}
