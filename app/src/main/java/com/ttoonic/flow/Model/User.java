package com.ttoonic.flow.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.Serializable;

public class User  implements Parcelable {

    private String username;
    private String phone_number;
    private String password;
    private String team;

    public User() {
    }

    public User(String username
            , String password
            , String phone
                ,String team
    ) {
        this.team = team;
     this.username = username;
    this.password = password;
    this.phone_number = phone;
    }

    public String getTeam() {
        return team;
    }

    protected User(Parcel in) {
        username = in.readString();
        phone_number = in.readString();
        password = in.readString();
        team = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(phone_number);
        dest.writeString(password);
        dest.writeString(team);
    }
}
