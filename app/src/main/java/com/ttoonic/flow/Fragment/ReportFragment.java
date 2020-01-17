package com.ttoonic.flow.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Holder.HolderAdapter;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Interface.RecyclerInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

import java.util.ArrayList;
import java.util.Date;


public class ReportFragment extends BaseFragment implements View.OnClickListener,
        DatabaseInteractive,
        SwipeRefreshLayout.OnRefreshListener, RecyclerInteractive {
    private View view;
    private DatabaseInit databaseInit;
    private User user;
    private RecyclerView recyclerView;
    private Fault fault;
    private ArrayList<Fault> faults;
    private HolderAdapter holderAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String> id;

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
        this.swipeRefreshLayout = this.view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        this.id = new ArrayList<>();
        load_before();
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

    public void load_before(){
        CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                swipeRefreshLayout.setRefreshing(true);
            }
            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
                databaseInit = new DatabaseInit();
                databaseInit.addDatabaseSuccessListener(ReportFragment.this);
                databaseInit.get_incidents(user.getTeam());
                faults = new ArrayList<>();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDatabaseSuccess(boolean data, Object object, String message) {
        if (object instanceof ArrayList) {
            this.faults = (ArrayList<Fault>) object;
            for(Fault fault1 : this.faults) {
               if (fault1.getType().equals("Incident") && !(fault1.getCategory().equals(user.getTeam()))){
                   this.faults.remove(fault1);
              }
               else {
                   if (new Date().after(fault1.getExpiration())) {
                       this.faults.remove(fault1);

                   }
               }
            }
            if (this.recyclerView == null) {
                this.recyclerView = this.view.findViewById(R.id.recycler_view);
                this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                this.holderAdapter = new HolderAdapter(getContext(), this.faults,this,this.user.getUsername());
                this.recyclerView.setAdapter(this.holderAdapter);
            }
            this.holderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseInit.get_incidents(user.getTeam());
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onRecycleInteract(Object object) {
        this.interactive.onFragmentInteract(this,object);
    }
}
