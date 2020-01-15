package com.ttoonic.flow.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Holder.HolderAdapter;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

import java.util.ArrayList;


public class ReportFragment extends BaseFragment implements View.OnClickListener, DatabaseInteractive {
    private View view;
    private DatabaseInit databaseInit;
    private User user;
    private RecyclerView recyclerView;
    private Fault fault;
    private ArrayList<Fault> faults;
    private HolderAdapter holderAdapter;
    public ReportFragment(FragmentInteractive fragmentInteractive) {
        super(fragmentInteractive);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.view = inflater.inflate(R.layout.fragment_report_list,null);
    }
    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;
        this.databaseInit = new DatabaseInit();
        this.databaseInit.addDatabaseSuccessListener(this);
        this.databaseInit.get_incidents(this.user.getTeam());
        this.recyclerView = this.view.findViewById(R.id.recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.faults = new ArrayList<>();
        this.holderAdapter = new HolderAdapter(getContext(),this.faults);
        this.recyclerView.setAdapter(this.holderAdapter);


    }
    @Override
    public void activityCallback(Object object) {
        super.activityCallback(object);
        if(object instanceof User){
            this.user = ((User)object); }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDatabaseSuccess(boolean data, Object object, String message) {
    this.fault = ((Fault)object);
    this.faults.add(this.fault);
    this.holderAdapter.notifyDataSetChanged();
    }

}
