package com.ttoonic.flow.Interface;

public interface ActivityInteractive {
    void activityCallback(Object object);
    void activitySensorChanged(Object object);
    void activityAccuracyChanged(Object object, int accuracy);
}
