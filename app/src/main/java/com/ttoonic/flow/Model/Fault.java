package com.ttoonic.flow.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Fault implements Parcelable {
    private String title;
    private String description;
    private double credibility;
    private Date timestamp;
    private String imgpath;
    private String creator;

    public Fault(String title, String description, double credibility, Date timestamp, String imgpath, String creator) {
        this.title = title;
        this.description = description;
        this.credibility = credibility;
        this.timestamp = timestamp;
        this.imgpath = imgpath;
        this.creator = creator;
    }

    public Fault() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getCredibility() {
        return credibility;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getImgpath() {
        return imgpath;
    }

    public String getCreator() {
        return creator;
    }

    public static Creator<Fault> getCREATOR() {
        return CREATOR;
    }

    protected Fault(Parcel in) {
        title = in.readString();
        description = in.readString();
        credibility = in.readDouble();
        imgpath = in.readString();
        creator = in.readString();
    }

    public static final Creator<Fault> CREATOR = new Creator<Fault>() {
        @Override
        public Fault createFromParcel(Parcel in) {
            return new Fault(in);
        }

        @Override
        public Fault[] newArray(int size) {
            return new Fault[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(credibility);
        dest.writeString(imgpath);
        dest.writeString(creator);
    }
}
