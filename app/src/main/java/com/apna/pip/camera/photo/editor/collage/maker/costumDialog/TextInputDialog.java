package com.apna.pip.camera.photo.editor.collage.maker.costumDialog;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apna.pip.camera.photo.editor.collage.maker.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


public class TextInputDialog extends AppCompatActivity {
    EditText mTextDialog;
    Button mOkDialog, mCancelDialog;
    Button mColorDialog;
    Button mTextFontDialog;
    TextView sampleTextDialog;

    protected int mColor = 0;

    String fontName = null, text;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.text_input_dialog);

        mTextDialog = findViewById(R.id.custom_dialog_text);
        mOkDialog = findViewById(R.id.costum_dialog_ok);
        mCancelDialog = findViewById(R.id.costum_dialog_cancel);
        mColorDialog = findViewById(R.id.custom_dialog_text_color);
        mTextFontDialog = findViewById(R.id.custom_dialog_text_type_face);
        sampleTextDialog = findViewById(R.id.custom_dialog_sampe_text);


        listeners();

    }

    private void listeners() {
        mOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = mTextDialog.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Text cannot be empty", Toast.LENGTH_SHORT).show();
                } else {

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("text", text);
                    returnIntent.putExtra("font", fontName);
                    returnIntent.putExtra("color", mColor);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        mCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        mColorDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getColour();

            }
        });

        mTextFontDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFont();
            }
        });

        mTextDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sampleTextDialog.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sampleTextDialog.getText().toString().isEmpty()) {
                    sampleTextDialog.setText("Type Something...");
                }

            }
        });

    }

    private void setFont() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TextInputDialog.this);
        builder.setTitle("Select Font").setItems(R.array.fonts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fontName = getResources().getStringArray(R.array.fonts)[which];
                sampleTextDialog.setTypeface(Typeface.createFromAsset(getAssets(), fontName + ".ttf"));
            }
        }).create().show();
    }

    private void getColour() {

        ColorPickerDialogBuilder.with(TextInputDialog.this).setTitle("Choose Color").wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
            }
        }).setPositiveButton("ok", new ColorPickerClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                mColor = selectedColor;
                sampleTextDialog.setTextColor(selectedColor);
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).build().show();
    }
}

