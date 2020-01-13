package com.ttoonic.flow;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Model.User;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public final class DatabaseInit {
   private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
   private static DatabaseInteractive databaseInteractive;

    public FirebaseFirestore getDb() {
        return db;
    }

    public DatabaseInit() {
    }

    public void addDatabaseSuccessListener(DatabaseInteractive databaseInteractive){
        this.databaseInteractive = databaseInteractive;
    }

    public boolean create_user(final User user){
        Map<String,Object> db_user = new HashMap<>();
        db_user.put("username",user.getUsername());
        db_user.put("password",user.getPassword());
        db_user.put("phone",user.getPhone_number());
        db_user.put("team",user.getTeam());

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isComplete()){

                    if (databaseInteractive != null) {
                        databaseInteractive.onDatabaseSuccess(true,user,"Success");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(databaseInteractive != null){
                    databaseInteractive.onDatabaseSuccess(false,101,"Error has Occured");
                }
            }
        });
        return false;
    }

    public boolean login_user(final String username,final String password,final String entry) throws UnsupportedEncodingException {
        byte[] decode_password = Base64.decode(password,Base64.DEFAULT);
        String dp = new String(decode_password,"UTF-8");
        db.collection("users").whereEqualTo("username",username).whereEqualTo("password",dp)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        if(documentSnapshot.contains("team")){
                         if(databaseInteractive != null){
                            databaseInteractive.onDatabaseSuccess(false,documentSnapshot.toObject(User.class),"Already exist");
                            break;
                         }
                        }
                        else {
                            if(databaseInteractive != null){
                                databaseInteractive.onDatabaseSuccess(false,null,"Unable to login into this field.");
                                break;
                            }
                        }
                        break;
                    }

                }
            }
        });
        return false;
    }

    public void check_data(final User user){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("username").equals(user.getUsername())){
                                    if(databaseInteractive != null){
                                        databaseInteractive.onDatabaseSuccess(false,1,"Already exist");
                                    }
                                    break;
                                }
                                else{
                                    create_user(user);
                                    break;
                                }
                            }
                        } else {
                            if(databaseInteractive != null){
                                databaseInteractive.onDatabaseSuccess(false,101,"Error has occured");
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(databaseInteractive != null){
                    databaseInteractive.onDatabaseSuccess(false,101,"Error has occured " + e.toString());
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                if (databaseInteractive != null) {
                    databaseInteractive.onDatabaseSuccess(true, 2,"Canceled");
                }
            }
        })
        ;

    }

}
