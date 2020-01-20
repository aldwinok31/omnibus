package com.ttoonic.flow.Service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ttoonic.flow.Activity.Activity_SMS;


public class SMSReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        String title ="";
        String data = "";
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "n";
                title = msgs[i].getOriginatingAddress();
                data =  msgs[i].getMessageBody();

            }
            //---display the new SMS message---
            if(title.equals("CB messages") || title.equals("NDRRMC") || title.equals("ndrrmc")) {
                Intent intent1 = new Intent(context, Activity_SMS.class);
                intent1.putExtra("Originated", title);
                intent1.putExtra("Body", data);
                context.startActivity(intent1);
            }
        }
    }



}
