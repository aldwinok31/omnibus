package com.ttoonic.flow.Interface;

import com.google.firebase.firestore.DocumentSnapshot;

public interface DatabaseInteractive {
    void onDatabaseSuccess(boolean data,Object object,String message);
}
