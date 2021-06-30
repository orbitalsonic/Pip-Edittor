package com.apna.pip.camera.photo.editor.collage.maker.recyleradapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apna.pip.camera.photo.editor.collage.maker.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditorAdapterString extends RecyclerView.Adapter<EditorAdapterString.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final EditorAdapter.OnItemClickListener listener;
    private ArrayList<String> list;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.editor_recycler_image);
        }

        public void bind(final String item, final int position, final EditorAdapter.OnItemClickListener listener) {
            InputStream inputstream = null;
            try {
                inputstream = mContext.getAssets().open("stickers/" + list.get(position));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = Drawable.createFromStream(inputstream, null);
            imageView.setImageDrawable(drawable);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

    }


    public EditorAdapterString(Context context, ArrayList<String> arrayList, EditorAdapter.OnItemClickListener listener) {
        mContext = context;
        list = arrayList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_recycler_item, parent, false);

        return new EditorAdapterString.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(list.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
