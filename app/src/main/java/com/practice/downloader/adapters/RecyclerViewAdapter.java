package com.practice.downloader.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.practice.downloader.R;

import java.util.List;

/**
 * Created by sudavid on 1/22/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageCell> {

    private Context context;
    private List<Bitmap> images;

    public RecyclerViewAdapter(Context context, List<Bitmap> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ImageCell onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cell = inflater.inflate(R.layout.recycler_cell, parent, false);
        ImageCell imageCell = new ImageCell(cell);
        return imageCell;
    }

    @Override
    public void onBindViewHolder(ImageCell holder, int position) {
        holder.setImageView(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public  static class ImageCell extends RecyclerView.ViewHolder {

        private ImageView imageView;


        public ImageCell(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void setImageView(Bitmap image) {
            imageView.setImageBitmap(image);
        }
    }
}
