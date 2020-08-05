package com.novankrisnady.rentalstudioband.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Studio implements Parcelable {
    private String name,address,studioId;

    public Studio() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStudioId() {
        return studioId;
    }

    public void setStudioId(String studioId) {
        this.studioId = studioId;
    }

    public static Creator<Studio> getCREATOR() {
        return CREATOR;
    }

    protected Studio(Parcel in) {
        name = in.readString();
        address = in.readString();
        studioId = in.readString();
    }

    public static final Creator<Studio> CREATOR = new Creator<Studio>() {
        @Override
        public Studio createFromParcel(Parcel in) {
            return new Studio(in);
        }

        @Override
        public Studio[] newArray(int size) {
            return new Studio[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(studioId);
    }
}
