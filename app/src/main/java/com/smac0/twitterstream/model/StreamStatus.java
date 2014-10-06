
package com.smac0.twitterstream.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable object representing a streamed twitter status
 * 
 * @author rahulchaudhari
 */
public class StreamStatus implements Parcelable {

    public static final Parcelable.Creator<StreamStatus> CREATOR = new Parcelable.Creator<StreamStatus>() {
        public StreamStatus createFromParcel(Parcel in) {
            return new StreamStatus(in);
        }

        public StreamStatus[] newArray(int size) {
            return new StreamStatus[size];
        }
    };

    private String mUserName;

    private String mStatus;

    public StreamStatus(String userName, String status) {
        mUserName = userName;
        mStatus = status;
    }

    private StreamStatus(Parcel in) {
        mUserName = in.readString();
        mStatus = in.readString();
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mUserName);
        out.writeString(mStatus);
    }
}
