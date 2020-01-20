package com.ttoonic.flow.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.ttoonic.flow.Interface.FragmentInteractive;

public class MapFragment extends SupportMapFragment {
    private FragmentInteractive fragmentInteractive;
    public MapFragment(FragmentInteractive fragmentInteractive,OnMapReadyCallback onMapReadyCallback) {
        this.fragmentInteractive = fragmentInteractive;
        getMapAsync(onMapReadyCallback);
    }

    @Override
    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        super.getMapAsync(onMapReadyCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
