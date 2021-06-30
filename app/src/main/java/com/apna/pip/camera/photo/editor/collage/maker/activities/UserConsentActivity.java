package com.apna.pip.camera.photo.editor.collage.maker.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apna.pip.camera.photo.editor.collage.maker.R;
import com.apna.pip.camera.photo.editor.collage.maker.utils.SharedPrefsUtils;


public class UserConsentActivity extends AppCompatActivity {
    Button button;
    CheckBox checkBox;
    TextView privacyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_dialog);

        button = findViewById(R.id.button);
        checkBox = findViewById(R.id.checkBox);
        privacyText=findViewById(R.id.privacyText);

        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://sites.google.com/view/grafixdezign/home");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    button.setAlpha(1);
                }else {
                    button.setAlpha((float) 0.5);
                }

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    SharedPrefsUtils.privacy_setter(UserConsentActivity.this,false);
                    Intent intent = new Intent(UserConsentActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

}
