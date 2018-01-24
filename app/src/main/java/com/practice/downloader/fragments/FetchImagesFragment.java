package com.practice.downloader.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.practice.downloader.models.Photo;
import com.practice.downloader.models.Root;
import com.practice.downloader.restinterfaces.ImagesInterface;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by sudavid on 1/22/18.
 */

public class FetchImagesFragment extends Fragment {

    private static final String TAG = "FetchImagesFragment";

    private List<Bitmap> bitmaps;


    public interface FetchImages {
        void notifyAdapterDataChanged();
    }

    private FetchImages callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Attaching fragment to activity");
        callback = (FetchImages) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        callback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void executeTask(String searchTerm, List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        new GetImagesRetainedTask(searchTerm, bitmaps).execute();
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public class GetImagesRetainedTask extends AsyncTask<Void, Bitmap, List<Bitmap>> {

        private static final String TAG = "GetImagesRetainedTask";

        private final String farm = "http://farm";
        private String center = ".static.flickr.com/";
        private String slash = "/";
        private String end = ".jpg";

        private ImagesInterface imagesInterface;
        private String searchTerm;
        private Root root;
        private List<Bitmap> bitmaps;

        public GetImagesRetainedTask(String searchTerm, List<Bitmap> bitmaps) {
            this.searchTerm = searchTerm;
            this.bitmaps = bitmaps;
        }


        @Override
        protected List<Bitmap> doInBackground(Void... voids) {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.flickr.com/services").build();
            imagesInterface = restAdapter.create(ImagesInterface.class);
            root = imagesInterface.questions(searchTerm);
            Log.d(TAG, "Number of photos: " + root.photos.photos.size());
            for (Photo photo : root.photos.photos) {
                String url = farm + photo.farm + center + photo.mServer + slash + photo.mId + "_" + photo.mSecret + end;
                Bitmap bitmap = null;
                try {
                    Log.d(TAG, "Loading image from URL: " + url);
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                } catch (Exception e) {
                }

                publishProgress(bitmap);
            }
            return bitmaps; // Not particularly necessary
        }

        @Override
        protected void onProgressUpdate(Bitmap... bitmapsArray) {
            Log.d(TAG, "Adding bitmap to list");
            bitmaps.add(bitmapsArray[0]);
            if(callback != null) callback.notifyAdapterDataChanged();
            else Log.d(TAG, "Activity is null; cannot update right now");
        }

        @Override
        protected void onPostExecute(List<Bitmap> images) {

        }

    }
}
