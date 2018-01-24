package com.practice.downloader.restinterfaces;

import com.practice.downloader.models.Root;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by sudavid on 1/22/18.
 */

public interface ImagesInterface {

    @GET("/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1")
    Root questions(@Query("text") String text);// @Query("text") String text);
}
