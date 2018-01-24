package com.practice.downloader.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sudavid on 1/22/18.
 */

public class PhotoCollection {

    public int page;
    public int pages;
    public int perpage;
    public int total;

    @SerializedName("photo")
    public List<Photo> photos;


}
