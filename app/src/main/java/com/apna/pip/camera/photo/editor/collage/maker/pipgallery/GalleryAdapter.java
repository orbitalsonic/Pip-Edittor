package com.apna.pip.camera.photo.editor.collage.maker.pipgallery;

import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apna.pip.camera.photo.editor.collage.maker.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener listener;
    private ArrayList<GalleryCollection> galleryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.gallery_recycler_image);
        }

        public void bind(final GalleryCollection item, final int position, final OnItemClickListener listener) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(galleryList.get(position).getImagePath()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

    }


    public GalleryAdapter(ArrayList<GalleryCollection> moviesList, OnItemClickListener listener) {
        galleryList = moviesList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_recycler_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(galleryList.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }
}

