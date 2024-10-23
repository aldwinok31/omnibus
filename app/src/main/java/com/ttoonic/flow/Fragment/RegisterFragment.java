package com.ttoonic.flow.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
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

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttoonic.flow.Activity.Activity_Fragment_Holder;
import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Interface.FragmentInteractive;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RegisterFragment extends BaseFragment implements View.OnClickListener, DatabaseInteractive, AdapterView.OnItemClickListener {
    private View view;
    private AlertDialog alertDialog;
    private String password;
    private String username;
    private String contact;
    public RegisterFragment(FragmentInteractive fragmentInteractive) {
        super(fragmentInteractive);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.view = inflater.inflate(R.layout.fragment_register,null);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.fragment = this;

        Button button = this.view.findViewById(R.id.create_submit);
        button.setOnClickListener(this);

        EditText editText = this.view.findViewById(R.id.team_selector);
        editText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.team_selector){
            showDialog();
        }
        if(v.getId() == R.id.create_submit) {

            EditText username = this.view.findViewById(R.id.user_name);
            EditText password = this.view.findViewById(R.id.pass_word);
            EditText confirm_password = this.view.findViewById(R.id.confirm_pass_word);
            EditText contact = this.view.findViewById(R.id.contact_number);
            EditText team = this.view.findViewById(R.id.team_selector);

            int valid_numbers = 0;
            if (username.getText().equals("") || username.getText().length() < 6) {
                username.setError("Invalid username");
                valid_numbers++;
            }
            if (password.getText().equals("") || password.getText().length() < 6) {
                password.setError("Invalid password");
                valid_numbers++;
            }
            if (confirm_password.getText().equals(password.getText())) {
                confirm_password.setError("Password not equal");
                valid_numbers++;
            }
            if (contact.getText().equals("") && contact.getText().length() < 10) {
                contact.setError("Invalid contact number");
                valid_numbers++;
            }
            if(team.getText().equals("") || team.getText().length() == 0){
                team.setError("Select a field to login");
                valid_numbers++;
            }

            if (valid_numbers == 0) {
                this.password = password.getText().toString();
                this.username = username.getText().toString();
                this.contact = contact.getText().toString();
                String data = team.getText().toString();
                byte[] decode_password = Base64.decode(this.password, Base64.DEFAULT);
                try {
                    String dp = new String(decode_password, "UTF-8");
                    User user = new User(this.username, dp, this.contact, data);
                    DatabaseInit databaseInit = new DatabaseInit();
                    databaseInit.addDatabaseSuccessListener(this);
                    databaseInit.check_data(user);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onDatabaseSuccess(boolean data , Object code,String message) {
        if(code instanceof User){
            Intent intent = new Intent(getContext(), Activity_Fragment_Holder.class);
            intent.putExtra("User_main", ((User)code));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().getApplicationContext().startActivity(intent);
        }else {
            Toast.makeText(this.view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(boolean data, String message) {

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
        if(alertDialog != null) {
            TextView textView = (TextView) view;
            String data = textView.getText().toString();
            EditText editText = this.view.findViewById(R.id.team_selector);
            editText.setText(data);
            this.alertDialog.dismiss();
        }
        }
}
