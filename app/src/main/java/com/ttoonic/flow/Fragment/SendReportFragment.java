package com.ttoonic.flow.Fragment;

import com.ttoonic.flow.Interface.FragmentInteractive;

public class SendReportFragment extends BaseFragment{
    public SendReportFragment(FragmentInteractive fragmentInteractive) {
        super(fragmentInteractive);
    }
    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;
    }
}
