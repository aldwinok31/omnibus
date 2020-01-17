package com.ttoonic.flow;

import android.content.SharedPreferences;
import android.location.Location;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(databaseInteractive != null){
                            databaseInteractive.onDatabaseSuccess(false,task.getResult().toString(),"Success");
                        }
                    }
                });

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

    public void add_database_listener(final String category,final String name){
        db.collection("fault").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        Fault fault = queryDocumentSnapshot.toObject(Fault.class);
                        if(!fault.getType().equals("Incident")) {
                            if (!fault.getMarked_safe().contains(name) && !fault.getMarked_unsafe().contains(name)) {
                                if (databaseInteractive != null) {
                                    databaseInteractive.onDatabaseSuccess(false, fault, queryDocumentSnapshot.getId());
                                }
                            }
                        }
                        else{
                            if(fault.getCategory() == category) {
                                if (!fault.getMarked_safe().contains(name) && !fault.getMarked_unsafe().contains(name)) {
                                    if (databaseInteractive != null) {
                                        databaseInteractive.onDatabaseSuccess(false, fault, queryDocumentSnapshot.getId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

    }


    public void update_alert(final Object object,final ArrayList<String> id,String safe){
        for ( String i : id) {
            String user = ((User) object).getUsername();

            db.collection("fault").document(i).update(safe, FieldValue.arrayUnion(user))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (databaseInteractive != null) {
                                databaseInteractive.onDatabaseSuccess(false,true, "update");
                            }
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                }
            })
            ;
        }
    }

    public void get_incidents(final String category){
        db.collection("fault").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Fault> faults = new ArrayList<>();
                    faults.addAll( task.getResult().toObjects(Fault.class));

                        if (databaseInteractive != null) {
                            databaseInteractive.onDatabaseSuccess(false, faults,"");
                        }

            }
        });
    }
    public String get_path(String path){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(path).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (databaseInteractive != null) {
                    databaseInteractive.onDatabaseSuccess(false,task.getResult(), "get list");
                }
            }
        });
        return "";
    }

    public void get_user(final String name){
        db.collection("users").whereEqualTo("username",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (databaseInteractive != null) {
                        databaseInteractive.onDatabaseSuccess(false, documentSnapshot.toObject(User.class), "get list");
                    }
                }
            }
        });
    }
    public void update_location(String name, Location location){
    db.collection("gps_provider").document(name).set(location,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            if (databaseInteractive != null) {
                databaseInteractive.onDatabaseSuccess(false, aVoid, "Update GPS");
            }
        }
    });
    }

    public void get_locations(final String name){
        db.collection("gps_provider").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    if(queryDocumentSnapshot.getId() != name) {
                        if (databaseInteractive != null) {
                            databaseInteractive.onDatabaseSuccess(false,queryDocumentSnapshot.toObject(Location.class)
                                    ,"Update" );
                        }
                    }
                }
            }
        });
    }

}
