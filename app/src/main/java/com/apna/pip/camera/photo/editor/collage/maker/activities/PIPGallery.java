package com.apna.pip.camera.photo.editor.collage.maker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.apna.pip.camera.photo.editor.collage.maker.pipgallery.GalleryAdapter;
import com.apna.pip.camera.photo.editor.collage.maker.pipgallery.GalleryCollection;
import com.apna.pip.camera.photo.editor.collage.maker.R;

import java.io.File;
import java.util.ArrayList;

public class PIPGallery extends AppCompatActivity {
    RecyclerView mRecyclerViewGallery;
    RecyclerView.LayoutManager mLayoutManager;
    GalleryAdapter mAdapter;
    ArrayList<GalleryCollection> mGalleryCollection;
    TextView textView;


    @Override
    protected void onStart() {
        super.onStart();
        settingUpGallery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipgallery);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (mGalleryCollection==null){
            mGalleryCollection=new ArrayList();
        }

        mRecyclerViewGallery = findViewById(R.id.gallery_recycler_view);
        textView = findViewById(R.id.text_view);

        mRecyclerViewGallery.hasFixedSize();
        mLayoutManager = new GridLayoutManager(PIPGallery.this, 2, 1, false);
        mRecyclerViewGallery.setLayoutManager(mLayoutManager);
        mGalleryCollection = new ArrayList<>();

    }

    private void settingUpGallery() {
        if (mGalleryCollection==null){
            mGalleryCollection=new ArrayList();
        }
        mGalleryCollection.clear();
        if (pickImagesFromFolder()) {
            mAdapter = new GalleryAdapter(mGalleryCollection, new GalleryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(PIPGallery.this, GalleryFullImage.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }
        if (mGalleryCollection.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            mRecyclerViewGallery.setVisibility(View.GONE);
        } else {
            mRecyclerViewGallery.setAdapter(mAdapter);
        }
    }

    private boolean pickImagesFromFolder() {
        String dirPath = getExternalFilesDir(null).getAbsolutePath() + "/PIPCamera";
        File filePath = new File(dirPath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        if (filePath.isDirectory()) {
            File imageList[] = filePath.listFiles();

            for (int i = imageList.length - 1; i >= 0; i--) {
                mGalleryCollection.add(new GalleryCollection(imageList[i].getAbsolutePath()));
            }
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
