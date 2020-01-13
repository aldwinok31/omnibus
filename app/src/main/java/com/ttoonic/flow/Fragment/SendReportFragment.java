package com.ttoonic.flow.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

import java.io.ByteArrayOutputStream;

public class SendReportFragment extends BaseFragment implements View.OnClickListener{
    private View view;
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

    }
    @Override
    public void activityCallback(Object object) {
        super.activityCallback(object);
        if(object instanceof User){
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                byteArray.length);
        ImageView imageView = this.view.findViewById(R.id.image_capture);
        imageView.setImageBitmap(bitmap);


        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       startActivityForResult(intent,1001);
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
}
