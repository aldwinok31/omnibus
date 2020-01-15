package com.ttoonic.flow.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Fault implements Parcelable {
    private String title;
    private String description;
    private double credibility;
    private Date timestamp;
    private String imgpath;
    private String type;
    private String creator;
    private String category;
    private ArrayList<String> marked_safe;
    private ArrayList<String> marked_unsafe;

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Fault(String title,String category, String description, String type,
                 double credibility, Date timestamp,
                 String imgpath, String creator,ArrayList<String> marked, ArrayList<String> mark_unsafe) {
        this.title = title;
        this.description = description;
        this.credibility = credibility;
        this.timestamp = timestamp;
        this.imgpath = imgpath;
        this.creator = creator;
        this.type = type;
        this.category = category;
        this.marked_safe = marked;
        this.marked_unsafe = mark_unsafe;
    }

    public ArrayList<String> getMarked_safe() {
        return marked_safe;
    }

    public void setMarked_safe( ArrayList<String> marked_safe) {
        this.marked_safe = marked_safe;
    }

    public  ArrayList<String> getMarked_unsafe() {
        return marked_unsafe;
    }

    public void setMarked_unsafe( ArrayList<String> marked_unsafe) {
        this.marked_unsafe = marked_unsafe;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCredibility(double credibility) {
        this.credibility = credibility;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
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
