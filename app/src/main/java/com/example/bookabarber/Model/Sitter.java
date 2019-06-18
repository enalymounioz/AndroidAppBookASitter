package com.example.bookabarber.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sitter implements Parcelable {
    private String name, username, password, sitterId;
    private Long rating;

    public Sitter(){

    }

    protected Sitter(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        sitterId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
    }

    public static final Creator<Sitter> CREATOR = new Creator<Sitter>() {
        @Override
        public Sitter createFromParcel(Parcel in) {
            return new Sitter(in);
        }

        @Override
        public Sitter[] newArray(int size) {
            return new Sitter[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getSitterId() {
        return sitterId;
    }

    public void setSitterId(String sitterId) {
        this.sitterId = sitterId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(sitterId);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rating);
        }
    }
}
