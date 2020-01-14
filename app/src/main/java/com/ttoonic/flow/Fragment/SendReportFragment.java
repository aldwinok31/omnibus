package com.ttoonic.flow.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class SendReportFragment extends BaseFragment implements View.OnClickListener, DatabaseInteractive {
    private View view;
    private User user;
    public SendReportFragment(FragmentInteractive fragmentInteractive) {
        super(fragmentInteractive);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.view = inflater.inflate(R.layout.fragment_report,null);
    }
    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;
        ImageButton imageButton = this.view.findViewById(R.id.camera_selector);
        imageButton.setOnClickListener(this);
        Button button = this.view.findViewById(R.id.report_submit);
        button.setOnClickListener(this);
        View dom_view = this.view.findViewById(R.id.dom_holder);
        CardView fire = dom_view.findViewById(R.id.card_fire);
        CardView flood = dom_view.findViewById(R.id.card_flood);
        CardView quake = dom_view.findViewById(R.id.card_quake);
        CardView incident = dom_view.findViewById(R.id.card_incident);
        fire.setOnClickListener(this);
        flood.setOnClickListener(this);
        quake.setOnClickListener(this);
        incident.setOnClickListener(this);
    }
    @Override
    public void activityCallback(Object object) {
        super.activityCallback(object);
        if(object instanceof User){
            this.user = (User) object;
            TextView textView = this.view.findViewById(R.id.submit_as);
            textView.setText("Submitting as a " + this.user.getTeam());
        }

    }

    // image holder

    private Bitmap bitmap;
    private byte[] mbytes;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        mbytes = stream.toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(mbytes, 0,
                mbytes.length);

        ImageView imageView = this.view.findViewById(R.id.image_capture);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(this);

        View view = this.view.findViewById(R.id.capture_holder);
        view.setVisibility(View.GONE);


        }
    }

    private  String persistImage(Bitmap bitmap, String name) {
        File filesDir = Environment.getExternalStorageDirectory();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile.getPath();
    }

    public void setbgreset(View v){
        CardView cardView = v.findViewById(R.id.card_fire);
        CardView cardView1 = v.findViewById(R.id.card_incident);
        CardView cardView2 = v.findViewById(R.id.card_flood);
        CardView cardView3 = v.findViewById(R.id.card_quake);

        cardView.setCardBackgroundColor(Color.WHITE);
        cardView1.setCardBackgroundColor(Color.WHITE);
        cardView2.setCardBackgroundColor(Color.WHITE);
        cardView3.setCardBackgroundColor(Color.WHITE);

    }
    // SELECTOR
    private int selector = 0 ;
    private Fault fault;
    private DatabaseInit databaseInit;
    @Override
    public void onClick(View v) {
        View dom_view = this.view.findViewById(R.id.dom_holder);
        if(v.getId() == R.id.card_fire){
            setbgreset(dom_view);
            CardView cardView = dom_view.findViewById(v.getId());
            cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            selector = 1;
        }
        if(v.getId() == R.id.card_flood){
            setbgreset(dom_view);
            CardView cardView = dom_view.findViewById(v.getId());
            cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            selector = 2;
        }
        if(v.getId() == R.id.card_quake){
            setbgreset(dom_view);
            CardView cardView = dom_view.findViewById(v.getId());
            cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            selector = 3;
        }
        if(v.getId() == R.id.card_incident){
            setbgreset(dom_view);
            CardView cardView = dom_view.findViewById(v.getId());
            cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            selector = 4;
        }

        if(v.getId() == R.id.report_submit){

            if(selector == 0) {
                Toast.makeText(getContext(), "Please choose incident category.", Toast.LENGTH_SHORT).show();
                return;
            }

            String incident = "";
            switch (selector){
                case 1: incident = "Fire";
                break;
                case 2: incident = "Flood";
                break;
                case 3: incident = "Quake";
                break;
                default:
                    incident = "Incident";
                    break;
            }

            int valids = 0;

            EditText title = this.view.findViewById(R.id.title_incident);
            EditText desc = this.view.findViewById(R.id.desc_incident);

            if(title.getText().equals("")){
                valids++;
                title.setError("Invalid title");
            }
            if(desc.getText().equals("")){
                valids++;
                desc.setError("Must not be empty");
            }
            if(this.bitmap == null){
                valids++;
                Toast.makeText(getContext(), "Must upload image", Toast.LENGTH_SHORT).show();
                return;
            }
            if(this.mbytes == null){
                valids++;
                Toast.makeText(getContext(), "Upload not complete", Toast.LENGTH_SHORT).show();
                return;
            }
            if(valids == 0){

                    this.interactive.onFragmentInteract(this,true);

                    this.fault = new Fault();
                    this.fault.setTitle(title.getText().toString());
                    this.fault.setDescription(desc.getText().toString());
                    this.fault.setCredibility(0.0);
                    this.fault.setType(incident);
                    this.fault.setCreator(this.user.getUsername());
                    this.fault.setCategory(this.user.getTeam());


                    String file_path = persistImage(this.bitmap,this.fault.getTitle());
                    databaseInit = new DatabaseInit();
                    databaseInit.addDatabaseSuccessListener(this);
                    databaseInit.upload_incident(file_path,this.fault.getTitle());

            }

        }
        if(v.getId() == R.id.capture_holder || v.getId() == R.id.camera_selector){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1001);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDatabaseSuccess(boolean data, Object object, String message) {
        this.interactive.onFragmentInteract(this,false);
        if (object instanceof String){
            this.fault.setImgpath(object.toString());
            Date currentTime = Calendar.getInstance().getTime();
            this.fault.setTimestamp(currentTime);
            this.databaseInit.upload_incident_to_firestore(this.fault);
            return;
        }
        if (object instanceof Fault){
            Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
        }
    }
}
