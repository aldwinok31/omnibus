package com.ttoonic.flow.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Marked implements Parcelable {
    private User user;
    private String safe;
    private String username;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Marked(User user, String safe , String username) {
        this.user = user;
        this.safe = safe;
        this.username = username;
    }

    public Marked() {
    }

    protected Marked(Parcel in) {
    }

    public static final Creator<Marked> CREATOR = new Creator<Marked>() {
        @Override
        public Marked createFromParcel(Parcel in) {
            return new Marked(in);
        }

        @Override
        public Marked[] newArray(int size) {
            return new Marked[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
