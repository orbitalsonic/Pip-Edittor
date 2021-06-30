package com.apna.pip.camera.photo.editor.collage.maker.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.apna.pip.camera.photo.editor.collage.maker.R;
import com.apna.pip.camera.photo.editor.collage.maker.utils.SharedPrefsUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    ProgressBar progress_bar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_);

        FirebaseMessaging.getInstance().subscribeToTopic("GrafixDezign");

        progress_bar=findViewById(R.id.progress_bar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100){
                    progressStatus +=1;
                    try{
                        Thread.sleep(30);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progress_bar.setProgress(progressStatus);
                        }
                    });
                }
            }
        }).start();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                boolean firstTime= SharedPrefsUtils.pivacy_getter(SplashActivity.this);
                if (firstTime){

                    Intent i = new Intent(SplashActivity.this, UserConsentActivity.class);
                    startActivity(i);
                    finish();

                }else
                {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        }, SPLASH_TIME_OUT);

    }

}
