package com.ttoonic.flow.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ttoonic.flow.Activity.Activity_Fragment_Holder;
import com.ttoonic.flow.Activity.Auth_Activity;
import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginFragment extends BaseFragment implements View.OnClickListener, DatabaseInteractive , AdapterView.OnItemClickListener {
    private View view;
    private String password;
    private String username;
    private AlertDialog alertDialog;
    public LoginFragment(FragmentInteractive fragmentInteractive) {
        super(fragmentInteractive);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.fragment_login,null);
    }


    @Override
    public void onClick(View v) {

        EditText username = view.findViewById(R.id.login_username);
        EditText password = view.findViewById(R.id.login_password);

        int valid = 0;
        if(username.getText().equals("") || username.getText().length() < 6){
            username.setError("Invalid username");
            valid++;
        }
        if(password.getText().equals("") || password.getText().length() < 6){
            password.setError("Invalid password");
            valid++;
        }

        this.username = username.getText().toString();
        this.password = password.getText().toString();

        if(valid == 0){
            showDialog();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;

        Button button = view.findViewById(R.id.submit_login);
        button.setOnClickListener(this);

    }

    @Override
    public void onDatabaseSuccess(boolean data, Object code, String message) {
        if(code instanceof User){
            Intent intent = new Intent(getContext(), Activity_Fragment_Holder.class);
            intent.putExtra("User_main", ((User)code));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().getApplicationContext().startActivity(intent);
        }
        else {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


    public void showDialog(){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_logas_picker,null);
        ListView view1 = view.findViewById(R.id.list_view);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Student");
        arrayList.add("Faculty");
        arrayList.add("Security");
        arrayList.add("Disaster Response");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,arrayList);

        view1.setAdapter(arrayAdapter);


        view1.setOnItemClickListener(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
        .setView(view).setIcon(R.drawable.mysafety).setTitle("Sign in as:");
        alert.create();
       alertDialog = alert.show();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(alertDialog != null){
            TextView textView = (TextView) view;
            String data = textView.getText().toString();
            try {
                DatabaseInit databaseInit = new DatabaseInit();
                databaseInit.addDatabaseSuccessListener(this);
                databaseInit.login_user(this.username,this.password,data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            alertDialog.dismiss();
        }

    }
}
