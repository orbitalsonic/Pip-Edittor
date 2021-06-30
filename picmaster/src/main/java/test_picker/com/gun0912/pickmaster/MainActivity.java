package test_picker.com.gun0912.pickmaster;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    private static final String TAG = "TedPicker";
    ArrayList<Uri> image_uris = new ArrayList<Uri>();

    ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermissionForSDK()) {
            Config config = new Config();
            config.setCameraHeight(R.dimen.ted_picker_camera_height);
            config.setToolbarTitleRes(R.string.custom_title);
            config.setSelectionMin(2);
            if (getIntent().hasExtra("istwoimage")){
                config.setSelectionLimit(2);
            }else {
                config.setSelectionLimit(3);
            }
            config.setSelectedBottomHeight(R.dimen.bottom_height);
            config.setFlashOn(true);

            getImages(config);
        }


    }


    private void getImages(Config config) {
        ImagePickerActivity.setConfig(config);
        Intent intent = new Intent(this, ImagePickerActivity.class);

        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }


        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES) {
            if (resultCode == Activity.RESULT_OK) {
                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                Intent returnIntent = new Intent();
                if (image_uris != null) {
                    returnIntent.putParcelableArrayListExtra("result", image_uris);
                    setResult(Activity.RESULT_OK, returnIntent);
                }
                finish();
            }else {
                finish();
            }
        }

    }


    private boolean checkPermissionForSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionWrite = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            Log.e("LogPermission", permissionWrite + "/m");
            int permerssionRecordAudio = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
            Log.e("LogPermission", permerssionRecordAudio + "/m");

            list = new ArrayList();
            if (permissionWrite != 0) {
                list.add("android.permission.WRITE_EXTERNAL_STORAGE");
            }
            if (permerssionRecordAudio != 0) {
                list.add("android.permission.CAMERA");
            }
            if (list.size() > 0) {
                ActivityCompat.requestPermissions(this, (String[]) list.toArray(new String[list.size()]), CredentialsApi.ACTIVITY_RESULT_OTHER_ACCOUNT);
                return false;
            } else {
                return true;
            }
        } else return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (perms.size() == list.size()) {
            Config config = new Config();
            config.setCameraHeight(R.dimen.ted_picker_camera_height);
            config.setToolbarTitleRes(R.string.custom_title);
            config.setSelectionMin(2);
            config.setSelectionLimit(4);
            config.setSelectedBottomHeight(R.dimen.bottom_height);
            config.setFlashOn(true);

            getImages(config);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        finish();
    }
}
