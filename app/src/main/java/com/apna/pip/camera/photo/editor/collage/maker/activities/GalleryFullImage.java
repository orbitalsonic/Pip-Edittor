package com.apna.pip.camera.photo.editor.collage.maker.activities;

import android.content.DialogInterface;
import android.os.Environment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.apna.pip.camera.photo.editor.collage.maker.pipgallery.GalleryCollection;
import com.apna.pip.camera.photo.editor.collage.maker.R;
import com.apna.pip.camera.photo.editor.collage.maker.pipgallery.SlidingImage_Adapter;

import java.io.File;
import java.util.ArrayList;

public class GalleryFullImage extends AppCompatActivity {

    private String path;
    private int position;
    int defPosition;

    private static ViewPager mPager;


    ArrayList<GalleryCollection> mGalleryCollection;


    private void makeBuilder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryFullImage.this);
        builder.setMessage("Are You Sure ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delImage();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    private void delImage() {
        path = mGalleryCollection.get(mPager.getCurrentItem()).getImagePath();
        File file = new File(path);
        if (file.delete()) {
            defPosition = mPager.getCurrentItem();
            mGalleryCollection.clear();
            pickImagesFromFolder();
            if (mGalleryCollection.isEmpty()) {
                finish();
            } else {
                init();
            }
        } else {
            Toast.makeText(this, "Image not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_full_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        defPosition = getIntent().getIntExtra("position", 0);

        mGalleryCollection = new ArrayList<>();

        mPager = findViewById(R.id.pager_gallery_full_image);

        pickImagesFromFolder();
        init();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_del:
                makeBuilder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {


        mPager.setAdapter(new SlidingImage_Adapter(GalleryFullImage.this, mGalleryCollection));
        mPager.setCurrentItem(defPosition);
    }

    private boolean pickImagesFromFolder() {
        String dirPath = getExternalFilesDir(null).getAbsolutePath() + "/PIPCamera";
        File filePath = new File(dirPath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        if (filePath.isDirectory()) {
            File imageList[] = filePath.listFiles();
            if (imageList==null)
                return false;
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
}
