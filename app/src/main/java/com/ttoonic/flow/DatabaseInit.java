package com.ttoonic.flow;

import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.Model.Marked;
import com.ttoonic.flow.Model.User;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

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

           byte[] decode_password ;
           try {
               decode_password = Base64.decode(password, Base64.DEFAULT);
           }catch (IllegalArgumentException e){
               if (databaseInteractive != null) {
                   databaseInteractive.onDatabaseSuccess(false, null, "Authentication failed.");
               }
               return  false;
           }
           String dp = new String(decode_password, "UTF-8");
           db.collection("users").whereEqualTo("username", username).whereEqualTo("password", dp)
                   .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isComplete()) {
                       for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                           if (documentSnapshot.contains("team")) {
                               if (databaseInteractive != null) {
                                   User object = documentSnapshot.toObject(User.class);
                                   object.setTeam(entry);
                                   databaseInteractive.onDatabaseSuccess(false, object, "Already exist");
                                   break;
                               }
                           } else {
                               if (databaseInteractive != null) {
                                   databaseInteractive.onDatabaseSuccess(false, null, "Unable to login into this field.");
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
                .whereEqualTo("phone_number",user.getPhone_number())
                .whereEqualTo("username",user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()){
                    if(task.getResult().isEmpty()){
                        create_user(user);
                    }
                    else {
                        if(databaseInteractive != null){
                            databaseInteractive.onDatabaseSuccess(false,1,"Already exist");
                        }
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
        });

    }


    public void upload_incident(final String path, final String name){
     StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        Uri file = Uri.fromFile(new File(path));
        StorageReference riversRef = storageReference.child("images/".concat(name));

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                String downloadUrl = task.getResult().getMetadata().getPath();
                if(databaseInteractive != null){
                    databaseInteractive.onDatabaseSuccess(false,downloadUrl,"Success");
                }
            }
             }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        if(databaseInteractive != null){
                            databaseInteractive.onDatabaseSuccess(false,101,"Error has Occured");
                        }
                    }
                });
    }

    public void upload_incident_to_firestore(final Fault fault){
        Map<String,Object> faults = new HashMap<>();
        fault.setMarked(new Marked());
       faults.put("title",fault.getTitle());
       faults.put("desc",fault.getDescription());
       faults.put("timestamp",fault.getTimestamp());
       faults.put("type",fault.getType());
        faults.put("credibility",fault.getCredibility());
        faults.put("type",fault.getImgpath());

        db.collection("fault")
                .add(fault)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isComplete()){
                    if (databaseInteractive != null) {
                        databaseInteractive.onDatabaseSuccess(true,fault,"Success");
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
    }

    public void add_database_listener(final String category){
        db.collection("fault").whereEqualTo("category",category).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        Fault fault = queryDocumentSnapshot.toObject(Fault.class);
                        if(fault.getCredibility() >= 0) {
                            if (databaseInteractive != null) {
                                databaseInteractive.onDatabaseSuccess(false, fault, queryDocumentSnapshot.getId());
                            }
                        }
                    }
                }
            }
        });
    }


    public void update_alert(final Object object,final ArrayList<String> id){
        for ( String i : id) {
            Marked marked = new Marked();
            marked.setSafe("Safe");
            marked.setUser((User) object);
            marked.setUsername(((User) object).getUsername());
            db.collection("fault").document(i).collection("marked").add(marked).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (databaseInteractive != null) {
                        databaseInteractive.onDatabaseSuccess(false,true, "update");
                    }
                }
            });
        }
    }
}
