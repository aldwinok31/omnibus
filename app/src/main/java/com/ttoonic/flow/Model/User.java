package com.ttoonic.flow.Model;

import android.util.Base64;

public class User {
    private String first_name;
    private String last_name;
    private String email_address;
    private Base64 password;
    public User(String first_name,String last_name,String email_address, Base64 password) {
    this.first_name = first_name;
    this.last_name = last_name;
    this.email_address = email_address;
    this.password = password;
    }
}
