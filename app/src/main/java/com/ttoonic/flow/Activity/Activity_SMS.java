package com.ttoonic.flow.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ttoonic.flow.MainActivity;
import com.ttoonic.flow.R;

public class Activity_SMS extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        if(getIntent().getExtras() != null) {
            if (!getIntent().getExtras().isEmpty()) {
                Button button = findViewById(R.id.button_sms);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                TextView textView = findViewById(R.id.sms_title);
                textView.setText(getIntent().getExtras().getString("Originated"));
                TextView textView1 = findViewById(R.id.content_sms);
                textView1.setText(getIntent().getExtras().getString("Body"));
                Vibrator vb = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(1000);
            }
        }
    }
}
