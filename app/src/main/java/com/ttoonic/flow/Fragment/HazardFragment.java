package com.ttoonic.flow.Fragment;

import android.graphics.Color;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;



public class HazardFragment extends BaseFragment{
    private User user;
    private View view;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    private int size = 0;
    public HazardFragment(FragmentInteractive fragmentInteractive) {
        super(fragmentInteractive);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.view = inflater.inflate(R.layout.fragment_hazard,null);
    }
    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;
        graphView = this.view.findViewById(R.id.graph_view);
        this.series = new LineGraphSeries<>(new DataPoint[] {
        });
        series.setAnimated(true);
        graphView.addSeries(series);
        graphView.setTitle("Vibration");
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphView.setTitleColor(getResources().getColor(R.color.colorPrimary));
    }
    @Override
    public void activityCallback(Object object) {
        super.activityCallback(object);
        if(object instanceof User){
            this.user = (User) object;
        }

    }

    @Override
    public void activitySensorChanged(Object object) {
        super.activitySensorChanged(object);
        if(object instanceof SensorEvent){
            float d = ((SensorEvent) object).values[0];
            this.series.appendData(new DataPoint(size++,d),false,50);
          TextView textView = this.view.findViewById(R.id.peak);
          textView.setText("Highest Peak: "+ this.series.getHighestValueY());

        }
    }

    @Override
    public void activityAccuracyChanged(Object object, int accuracy) {
        super.activityAccuracyChanged(object, accuracy);
    }


}
