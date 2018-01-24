package com.practice.downloader.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sudavid on 1/22/18.
 */

public class Photo {

    @SerializedName("id")
    public String mId;

    @SerializedName("secret")
    public String mSecret;

    @SerializedName("title")
    public String mTitle;

    @SerializedName("server")
    public int mServer;

    @SerializedName("farm")
    public int farm;

    public String toString() {
        return "Photo{" +
                "mId=" + mId +
                ", mSecret='" + mSecret + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mServer=" + mServer +
                '}';
    }

}
