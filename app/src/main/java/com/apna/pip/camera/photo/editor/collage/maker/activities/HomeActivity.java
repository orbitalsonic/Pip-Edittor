package com.apna.pip.camera.photo.editor.collage.maker.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import com.google.android.material.tabs.TabLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.apna.pip.camera.photo.editor.collage.maker.R;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int STORAGE_WRITE_PERMISSION = 99;

    private Uri mCropImageUri;

    int category = 0;

    Intent intent;
    ViewPager viewPager;
    TabLayout sliderDotspanel;
    private Integer [] images = {R.drawable.slider_1,R.drawable.slider_2,R.drawable.slider_3,R.drawable.slider_4};

    private ImageView[] dots;
    boolean gallery = false;
    boolean isRandom = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ConstraintLayout frame_const = findViewById(R.id.frame_const);
        ConstraintLayout gallery_const = findViewById(R.id.gallery_const);
        ConstraintLayout moreApps = findViewById(R.id.more_const);
        ConstraintLayout shape_const = findViewById(R.id.shape_const);
        ConstraintLayout ratteus_const = findViewById(R.id.ratteus_const);
        ConstraintLayout share_const = findViewById(R.id.share_const);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (TabLayout) findViewById(R.id.SliderDots);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);
        sliderDotspanel.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        frame_const.setOnClickListener(this);
        gallery_const.setOnClickListener(this);
        moreApps.setOnClickListener(this);
        shape_const.setOnClickListener(this);
        ratteus_const.setOnClickListener(this);
        share_const.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.frame_const:
                category = 999;
                if (isStorageWritePermissionGranted()) {
                    CropImage.startPickImageActivity(this);
                }
                break;
            case R.id.more_const:
//                category = 1000;
//                if (isStorageWritePermissionGranted()) {
//                    CropImage.startPickImageActivity(this);
//                }
                Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Grafix+Dezign");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.shape_const:
                category = 1001;
                if (isStorageWritePermissionGranted()) {
                    CropImage.startPickImageActivity(this);
                }
                break;

            case R.id.gallery_const:
                gallery = true;
                if (isStorageWritePermissionGranted()) {
                    gallery = false;
                    startActivity(new Intent(this, PIPGallery.class));
                }
                break;
            case R.id.ratteus_const:
                Uri uri3 = Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName());
                Intent intent3 = new Intent(Intent.ACTION_VIEW, uri3);
                startActivity(intent3);
                    break;


            case R.id.share_const:
                try {
                    Intent link = new Intent(Intent.ACTION_SEND);
                    link.setType("text/plain");
                    link.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String sAux = "https://play.google.com/store/apps/details?id="+getPackageName();
                    link.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(link, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

                    break;


        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                intent = new Intent(HomeActivity.this, PIPEditor.class);
                intent.putExtra("imageUri", resultUri.toString());
                intent.putExtra("category", category);
                startActivity(intent);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (requestCode == 421) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Uri> selectedImageForTwoImagePIP = data.getParcelableArrayListExtra("result");
                intent = new Intent(HomeActivity.this, TwoImagePipEditorAcitivity.class);
                intent.putParcelableArrayListExtra("result", selectedImageForTwoImagePIP);
                startActivity(intent);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == STORAGE_WRITE_PERMISSION) {
            if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(HomeActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            } else {
                if (gallery) {
                    gallery = false;
                    startActivity(new Intent(HomeActivity.this, PIPGallery.class));

                } else {
                    CropImage.startPickImageActivity(this);
                }
            }
        }

    }

    public boolean isStorageWritePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_WRITE_PERMISSION);
                return false;
            }
        } else {
            return true;
        }
    }


    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem()<images.length-1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle("Alert!");
        alertDialog.setIcon(R.drawable.tick);
        // Setting Yes Button
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"YES",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                //Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();


            }
        });

        // Setting No Button
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"NO",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();


            }
        });
// Showing Alert Message
        alertDialog.show();
//        Toast.makeText(getApplicationContext(),"back",Toast.LENGTH_SHORT).show();
//        finish();
    }
}
