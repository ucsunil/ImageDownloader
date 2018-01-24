package com.practice.downloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.practice.downloader.adapters.RecyclerViewAdapter;
import com.practice.downloader.models.Photo;
import com.practice.downloader.models.Root;
import com.practice.downloader.restinterfaces.ImagesInterface;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private RecyclerView recycler;
    private Button search;

    private Activity context;
    private List<Bitmap> bitmaps;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        editText = (EditText) findViewById(R.id.search_term);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        bitmaps = new ArrayList<>();
        adapter = new RecyclerViewAdapter(context, bitmaps);
        recycler.setAdapter(adapter);

        search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = editText.getText().toString();
                if(bitmaps.size() > 0) {
                    bitmaps.clear();
                    adapter.notifyDataSetChanged();
                }
                new GetImagesTask(searchTerm).execute();
            }
        });

    }

    public class GetImagesTask extends AsyncTask<Void, Bitmap, List<Bitmap>> {

        private final String farm = "http://farm";
        private String center = ".static.flickr.com/";
        private String slash = "/";
        private String end = ".jpg";

        private static final String TAG = "GetImagesTask";

        private ImagesInterface imagesInterface;
        private String searchTerm;
        private Root root;


        public GetImagesTask(String searchTerm) {
            this.searchTerm = searchTerm;
        }


        @Override
        protected List<Bitmap> doInBackground(Void... voids) {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.flickr.com/services").build();
            imagesInterface = restAdapter.create(ImagesInterface.class);
            root = imagesInterface.questions(searchTerm);
            Log.d(TAG, "Number of photos: " + root.photos.photos.size());
            for(Photo photo : root.photos.photos) {
                String url = farm + photo.farm + center + photo.mServer + slash + photo.mId + "_" + photo.mSecret + end;
                Bitmap bitmap = null;
                try {
                    Log.d(TAG, "Loading image from URL: " + url);
                    bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
                } catch (Exception e) {}

                publishProgress(bitmap);
            }
            return bitmaps; // Not particularly necessary
        }

        @Override
        protected void onProgressUpdate(Bitmap... bitmapsArray) {
            Log.d(TAG, "Adding bitmap to list");
            bitmaps.add(bitmapsArray[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<Bitmap> images) {

        }
    }
}
