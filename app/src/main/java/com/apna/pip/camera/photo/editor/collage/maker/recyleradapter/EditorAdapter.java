package com.apna.pip.camera.photo.editor.collage.maker.recyleradapter;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apna.pip.camera.photo.editor.collage.maker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private final OnItemClickListener listener;
    private ArrayList<EditorCollection> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.editor_recycler_image);
        }
        public void bind(final EditorCollection item, final int position, final OnItemClickListener listener) {
            Picasso.get().load(item.getImageID()).resize(75,75).onlyScaleDown().placeholder(R.drawable.place_holder).into(imageView);
//            imageView.setImageResource(item.getImageID());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

    }


    public EditorAdapter(ArrayList<EditorCollection> arrayList, OnItemClickListener listener) {
        list = arrayList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editor_recycler_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(list.get(position),position, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
