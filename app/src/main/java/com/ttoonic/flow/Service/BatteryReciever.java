package com.ttoonic.flow.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class BatteryReciever extends BroadcastReceiver {
    private BatteryReciever.onBatteryChange batteryChange;

    public BatteryReciever(BatteryReciever.onBatteryChange batteryChange) {
        this.batteryChange = batteryChange;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        float temperature = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
        if(this.batteryChange != null){
            this.batteryChange.onBatteryTempChange(temperature);
        }
    }

    public  interface onBatteryChange{
        void onBatteryTempChange(float temperature);
    }
}
