package com.practice.downloader;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.practice.downloader.adapters.RecyclerViewAdapter;
import com.practice.downloader.fragments.FetchImagesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sudavid on 1/23/18.
 */

public class RetainedDataActivity extends AppCompatActivity implements FetchImagesFragment.FetchImages {

    private static final String TAG = "RetainedDataActivity";

    private EditText editText;
    private RecyclerView recycler;
    private Button search;

    private Activity context;
    private List<Bitmap> bitmaps;
    private RecyclerViewAdapter adapter;

    private FetchImagesFragment fetchImagesFragment;
    private static final String TAG_FETCH_IMAGES_FRAGMENT = "fetchImagesFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        FragmentManager fm = getFragmentManager();
        fetchImagesFragment = (FetchImagesFragment) fm.findFragmentByTag(TAG_FETCH_IMAGES_FRAGMENT);

        if(fetchImagesFragment == null) {
            fetchImagesFragment = new FetchImagesFragment();
            fm.beginTransaction().add(fetchImagesFragment, TAG_FETCH_IMAGES_FRAGMENT).commit();
        } else {
            Log.d(TAG, "Fragment retained; no need to recreate");
        }

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
                fetchImagesFragment.executeTask(searchTerm, bitmaps);
            }
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle restoreInstanceState) {
        super.onRestoreInstanceState(restoreInstanceState);

        if(fetchImagesFragment != null) { // Means this is a restoration
            bitmaps = fetchImagesFragment.getBitmaps();
            adapter = new RecyclerViewAdapter(context, bitmaps);
            recycler.setAdapter(adapter);
        }
    }

    @Override
    public void notifyAdapterDataChanged() {
        adapter.notifyDataSetChanged();
    }
}
