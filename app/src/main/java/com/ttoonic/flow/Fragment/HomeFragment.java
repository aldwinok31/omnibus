package com.ttoonic.flow.Fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.MainActivity;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

public class HomeFragment extends BaseFragment  implements  View.OnClickListener{
    private View view;

    @Override
    public void activityCallback(Object object) {
        super.activityCallback(object);
        if(object instanceof User){
            TextView name_print = this.view.findViewById(R.id.user_name_print);
            TextView number_print = this.view.findViewById(R.id.phone_print);
            TextView team_print = this.view.findViewById(R.id.team_print);

            name_print.setText(((User) object).getUsername());
            number_print.setText(((User) object).getPhone_number());
            team_print.setText(((User) object).getTeam());
            this.view.findViewById(R.id.logout_button).setOnClickListener(this);
        }
    }

    @Override
    public void activityAccuracyChanged(Object object, int accuracy) {
        super.activityAccuracyChanged(object, accuracy);
    }

    @Override
    public void activitySensorChanged(Object object) {
        super.activitySensorChanged(object);
    }

    public HomeFragment(FragmentInteractive interactive) {
       super(interactive);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.view = inflater.inflate(R.layout.fragment_home,null);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout_button){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(getContext(), "Successfully signed out.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
