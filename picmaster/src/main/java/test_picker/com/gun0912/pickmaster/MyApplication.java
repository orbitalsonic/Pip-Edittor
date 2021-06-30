package test_picker.com.gun0912.pickmaster;

import android.app.Activity;
import android.content.Context;
import androidx.multidex.MultiDex;

public class MyApplication extends Activity {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}