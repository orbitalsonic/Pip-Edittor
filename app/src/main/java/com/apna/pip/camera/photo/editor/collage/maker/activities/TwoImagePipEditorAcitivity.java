package com.apna.pip.camera.photo.editor.collage.maker.activities;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.commit451.nativestackblur.NativeStackBlur;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.apna.pip.camera.photo.editor.collage.maker.collegeview.CollageView;
import com.apna.pip.camera.photo.editor.collage.maker.collegeview.MultiTouchListener;
import com.apna.pip.camera.photo.editor.collage.maker.costumDialog.TextInputDialog;
import com.apna.pip.camera.photo.editor.collage.maker.helper.filters.PipFilters;
import com.apna.pip.camera.photo.editor.collage.maker.helper.list.PipList;
import com.apna.pip.camera.photo.editor.collage.maker.recyleradapter.EditorAdapter;
import com.apna.pip.camera.photo.editor.collage.maker.recyleradapter.EditorAdapterString;
import com.apna.pip.camera.photo.editor.collage.maker.recyleradapter.EditorCollection;
import com.apna.pip.camera.photo.editor.collage.maker.saveimage.SaveImage;
import com.apna.pip.camera.photo.editor.collage.maker.sticker.view.BubbleTextView;
import com.apna.pip.camera.photo.editor.collage.maker.sticker.view.StickerView;
import com.apna.pip.camera.photo.editor.collage.maker.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class TwoImagePipEditorAcitivity extends AppCompatActivity {

    final int TEXT_FONT_COLOR = 856;
    final int PIP = 0;
    final int FILTERS = 1;

    private ArrayList<View> mViews;
    StickerView mCurrentView;
    private BubbleTextView mCurrentEditTextView, editBubbleView;


    SeekBar editorBlurIntensitySeekBar;
    Button editorPip, editorReplace, editorFilter, editorSticker, editorText, editorHideSeekBarContainer;
    ImageView editorMaskImage, editorFrameImage;
    CollageView editorForegroundImageLeft;
    CollageView editorForegroundImageRight;
    View editorSelectorForEditingImages, editorContainerForSeekBar;
    RelativeLayout editorViewToSave;

    private Uri mCropImageUri;

    Bitmap defualtBackImage;
    Bitmap preCurrentBackgroundImage, preCurrentForegroundImageLeft, preCurrentForegroundImageRight;
    Bitmap postCurrentForegroundImageLeft, postCurrentForegroundImageRight;

    RecyclerView editorRecycler;
    RecyclerView.LayoutManager recyclerManager;
    EditorAdapter recyclerAdapter;

    EditorAdapterString recyclerAdapterString;

    ArrayList<EditorCollection> currentFrameList, currentMaskList, filtersCollection;
    ArrayList<String> stickersList;

    int imageToApplyEffect = 0;
    int whichImage;

    private int currentMaskImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_image_pip_editor_acitivity);

//        MobileAds.initialize(this, getString(R.string.app_id));

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        componentInitializer();
        listenerInitializer();
        viewInitializer();
    }

    private void componentInitializer() {
        editorMaskImage = findViewById(R.id.editor_mask_image);
        editorForegroundImageLeft = findViewById(R.id.editor_foreground_image_left);
        editorForegroundImageRight = findViewById(R.id.editor_foreground_image_right);
        editorFrameImage = findViewById(R.id.editor_frame_image);
        editorBlurIntensitySeekBar = findViewById(R.id.editor_blur_intensity_seekbar);
        editorPip = findViewById(R.id.editor_pip);
        editorReplace = findViewById(R.id.editor_replace);
        editorFilter = findViewById(R.id.editor_filters);
        editorSticker = findViewById(R.id.editor_sticker);
        editorText = findViewById(R.id.editor_text);
        editorRecycler = findViewById(R.id.editor_recycler_view);
        editorSelectorForEditingImages = findViewById(R.id.editor_selector_for_editing_images);
        editorContainerForSeekBar = findViewById(R.id.editor_container_for_seek_bar);
        editorViewToSave = findViewById(R.id.editor_view_to_save);
        editorHideSeekBarContainer = findViewById(R.id.editor_hide_seekbar_container);


        currentFrameList = new ArrayList<>();
        currentMaskList = new ArrayList<>();

        currentFrameList = new PipList().getTwoImagePipFrameList();
        currentMaskList = new PipList().getTwoImagePipMaskList();


        filtersCollection = new ArrayList<>();
        filtersCollection = new PipList().getFiltersList();

        mViews = new ArrayList<>();

        try {
            String[] stickerImagesFromAssets = getAssets().list("stickers");
            stickersList = new ArrayList<String>(Arrays.asList(stickerImagesFromAssets));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenerInitializer() {
        editorForegroundImageLeft.setOnTouchListener(new MultiTouchListener(this));
        editorForegroundImageRight.setOnTouchListener(new MultiTouchListener(this));

        editorReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeBuilder();
            }
        });

        editorFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.FadeIn).duration(500).playOn(findViewById(R.id.editor_selector_for_editing_images));
                editorSelectorForEditingImages.setVisibility(View.VISIBLE);
                setRecyclerAdapter(filtersCollection, FILTERS);

            }
        });

        editorPip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.FadeOut).duration(300).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        editorSelectorForEditingImages.setVisibility(View.GONE);
                    }
                }).playOn(findViewById(R.id.editor_selector_for_editing_images));

                setRecyclerAdapter(new PipList().getTwoImagePipThumbnails(), PIP);
            }
        });

        editorSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.FadeOut).duration(300).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        editorSelectorForEditingImages.setVisibility(View.GONE);
                    }
                }).playOn(findViewById(R.id.editor_selector_for_editing_images));
                setRecyclerAdapter1();
            }
        });

        editorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBubble();
            }
        });

        editorViewToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideControl();
            }
        });


        editorBlurIntensitySeekBar.setProgress(25);
        editorBlurIntensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editorMaskImage.setImageBitmap(NativeStackBlur.process(preCurrentBackgroundImage, seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        editorHideSeekBarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editorContainerForSeekBar.setVisibility(View.GONE);
            }
        });


    }

    private void viewInitializer() {

        ArrayList<Uri> imageList = getIntent().getParcelableArrayListExtra("result");
        Uri uri = imageList.get(0);
        Bitmap bm = uriToBitmap(uri);

        defualtBackImage = bm;
        preCurrentBackgroundImage = bm;
        preCurrentForegroundImageLeft = bm;

        uri = imageList.get(1);
        bm = uriToBitmap(uri);
        preCurrentForegroundImageRight = bm;

        editorForegroundImageLeft.setImageBitmap(preCurrentForegroundImageLeft);
        editorForegroundImageRight.setImageBitmap(preCurrentForegroundImageRight);
        makeMaskImage(currentMaskList.get(4).getImageID(), currentFrameList.get(4).getImageID());

        editorRecycler.setHasFixedSize(true);
        recyclerManager = new LinearLayoutManager(TwoImagePipEditorAcitivity.this, 0, false);
        editorRecycler.setLayoutManager(recyclerManager);

        setRecyclerAdapter(new PipList().getTwoImagePipThumbnails(), PIP);
    }

    private void setRecyclerAdapter(ArrayList<EditorCollection> recyclerList, final int whichList) {
        recyclerAdapter = new EditorAdapter(recyclerList, new EditorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (whichList) {
                    case 0:
                        makeMaskImage(currentMaskList.get(position).getImageID(), currentFrameList.get(position).getImageID());
                        break;
                    case 1:
                        new FilterAsyncTask().execute(new Integer(position));
                        break;
                    case 3:
                        break;
                }
            }
        });

        editorRecycler.setAdapter(recyclerAdapter);
    }

    private void setRecyclerAdapter1() {
        recyclerAdapterString = new EditorAdapterString(TwoImagePipEditorAcitivity.this, stickersList, new EditorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                InputStream inputstream = null;
                try {
                    inputstream = getApplicationContext().getAssets().open("stickers/" + stickersList.get(position));
                    Bitmap sticker = BitmapFactory.decodeStream(inputstream);
                    addStickerView(sticker);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        editorRecycler.setAdapter(recyclerAdapterString);

    }


    private void makeBuilder() {
        String[] array = new String[]{"Background", "Left", "Right", "Cencel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TwoImagePipEditorAcitivity.this);
        builder.setTitle("Select Image").setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                    case 1:
                    case 2:
                        whichImage = which;
                        CropImage.startPickImageActivity(TwoImagePipEditorAcitivity.this);
                        break;
                    default:
                        break;
                }

            }
        }).setCancelable(true).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.editor_menu_save:
                boolean a = false;
                boolean b = false;
                if ((editorSelectorForEditingImages.getVisibility() == View.VISIBLE)) {
                    editorSelectorForEditingImages.setVisibility(View.GONE);
                    editorContainerForSeekBar.setVisibility(View.GONE);
                    if (mCurrentView != null) {
                        mCurrentView.setInEdit(false);
                    }
                    if (mCurrentEditTextView != null) {
                        mCurrentEditTextView.setInEdit(false);
                    }
                    if (new SaveImage().save(TwoImagePipEditorAcitivity.this, editorViewToSave)) {
                        Toast.makeText(TwoImagePipEditorAcitivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TwoImagePipEditorAcitivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editorContainerForSeekBar.setVisibility(View.GONE);
                    if (mCurrentView != null) {
                        mCurrentView.setInEdit(false);
                    }
                    if (mCurrentEditTextView != null) {
                        mCurrentEditTextView.setInEdit(false);
                    }
                    if (new SaveImage().save(TwoImagePipEditorAcitivity.this, editorViewToSave)) {
                        Toast.makeText(TwoImagePipEditorAcitivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TwoImagePipEditorAcitivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(TwoImagePipEditorAcitivity.this, PIPGallery.class));
                }
                return true;
            case R.id.editor_menu_control_blur_intensity:
                editorContainerForSeekBar.setVisibility(View.VISIBLE);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void addStickerView(Bitmap sticker) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setBitmap(sticker);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                editorViewToSave.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        editorViewToSave.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void addBubble() {
        final BubbleTextView bubbleTextView = new BubbleTextView(this, Color.BLACK, 0);
        bubbleTextView.setImageResource(R.mipmap.bubble_rb);
        bubbleTextView.setOperationListener(new BubbleTextView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(bubbleTextView);
                editorViewToSave.removeView(bubbleTextView);
            }

            @Override
            public void onEdit(BubbleTextView bubbleTextView) {
                if (mCurrentView != null) {
                    mCurrentView.setInEdit(false);
                }
                mCurrentEditTextView.setInEdit(false);
                mCurrentEditTextView = bubbleTextView;
                mCurrentEditTextView.setInEdit(true);
            }

            @Override
            public void onClick(BubbleTextView bubbleTextView) {
                editBubbleView = bubbleTextView;
                startActivityForResult(new Intent(TwoImagePipEditorAcitivity.this, TextInputDialog.class), TEXT_FONT_COLOR);
            }

            @Override
            public void onTop(BubbleTextView bubbleTextView) {
                int position = mViews.indexOf(bubbleTextView);
                if (position == mViews.size() - 1) {
                    return;
                }
                BubbleTextView textView = (BubbleTextView) mViews.remove(position);
                mViews.add(mViews.size(), textView);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        editorViewToSave.addView(bubbleTextView, lp);
        mViews.add(bubbleTextView);
        setCurrentEdit(bubbleTextView);
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

        if (requestCode == TEXT_FONT_COLOR && resultCode == Activity.RESULT_OK && data != null) {
            String text = data.getStringExtra("text");
            String fontName = data.getStringExtra("font");
            int color = data.getIntExtra("color", 0);
            Typeface typeface = null;
            if (fontName != null) {
                typeface = Typeface.createFromAsset(getAssets(), fontName + ".ttf");
            }
            editBubbleView.setText(text, color, typeface);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mCropImageUri = result.getUri();
                if (whichImage == 0) {
                    setBackgroundImage();
                } else if (whichImage == 1) {
                    setLeftImage();
                } else if (whichImage == 2) {
                    setRightImage();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void setBackgroundImage() {
        defualtBackImage = null;
        defualtBackImage = uriToBitmapRunning(mCropImageUri);
        changeBackgroundImage();
        editorContainerForSeekBar.setVisibility(View.VISIBLE);
    }

    private void setLeftImage() {
        editorContainerForSeekBar.setVisibility(View.GONE);
        postCurrentForegroundImageLeft = null;
        preCurrentForegroundImageLeft = uriToBitmapRunning(mCropImageUri);
        editorForegroundImageLeft.setImageBitmap(preCurrentForegroundImageLeft);
    }

    private void setRightImage() {
        editorContainerForSeekBar.setVisibility(View.GONE);
        postCurrentForegroundImageRight = null;
        preCurrentForegroundImageRight = uriToBitmapRunning(mCropImageUri);
        editorForegroundImageRight.setImageBitmap(preCurrentForegroundImageRight);
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    public void setImageToApplyEffect(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.editor_bothimages:
                if (checked) {
                    imageToApplyEffect = 0;
                }
                break;
            case R.id.editor_foregroundimageRightonly:
                if (checked) {
                    imageToApplyEffect = 2;
                }
                break;
            case R.id.editor_foregroundimageLeftonly:
                if (checked) {
                    imageToApplyEffect = 1;
                }
                break;
            default:
                if (checked) {
                    imageToApplyEffect = 0;
                }
                break;

        }
    }

    public void makeMaskImage(int maskimage, int frameimage) {
        currentMaskImage = maskimage;

        editorFrameImage.setBackgroundResource(frameimage);

        Bitmap mask = BitmapFactory.decodeResource(getResources(), maskimage);
        Bitmap original = resizeImage(mask.getWidth(), mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        editorBlurIntensitySeekBar.setProgress(25);
        editorMaskImage.setImageBitmap(NativeStackBlur.process(result, 25));
        preCurrentBackgroundImage = result;
    }

    public void changeBackgroundImage() {
        Bitmap mask = BitmapFactory.decodeResource(getResources(), currentMaskImage);
        Bitmap original = resizeImage(mask.getWidth(), mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        editorBlurIntensitySeekBar.setProgress(25);
        editorMaskImage.setImageBitmap(NativeStackBlur.process(result, 25));
        preCurrentBackgroundImage = result;

        if (postCurrentForegroundImageLeft != null) {
            editorForegroundImageLeft.setImageBitmap(postCurrentForegroundImageLeft);
        } else {
            editorForegroundImageLeft.setImageBitmap(preCurrentForegroundImageLeft);
        }
        if (postCurrentForegroundImageRight != null) {
            editorForegroundImageRight.setImageBitmap(postCurrentForegroundImageRight);
        } else {
            editorForegroundImageRight.setImageBitmap(preCurrentForegroundImageRight);
        }
    }

    public Bitmap resizeImage(int h, int w) {
        float scaleWidth;
        float scaleHeight;

        int width = defualtBackImage.getWidth();
        int height = defualtBackImage.getHeight();
        if (width < height) {
            scaleWidth = ((float) w) / ((float) width);
            scaleHeight = scaleWidth;
        } else {
            scaleHeight = ((float) h) / ((float) height);
            scaleWidth = scaleHeight;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        preCurrentBackgroundImage = Bitmap.createBitmap(defualtBackImage, 0, 0, width, height, matrix, false);
        return preCurrentBackgroundImage;

    }

    private Bitmap uriToBitmap(Uri uri) {

        Bitmap bm = BitmapFactory.decodeFile(uri.toString());

        return bm;
    }

    private Bitmap uriToBitmapRunning(Uri uri) {

        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            return bm;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);

    }

    private void setCurrentEdit(BubbleTextView bubbleTextView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentEditTextView = bubbleTextView;
        mCurrentEditTextView.setInEdit(true);
    }

    public void hideControl() {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
    }


    private class FilterAsyncTask extends AsyncTask<Integer, Void, Bitmap> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(TwoImagePipEditorAcitivity.this);
            dialog.setMessage("Applying..");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            switch (imageToApplyEffect) {
                case 0:
                    if (postCurrentForegroundImageLeft != null) {
                        editorForegroundImageLeft.setImageBitmap(postCurrentForegroundImageLeft);
                    } else {
                        editorForegroundImageLeft.setImageBitmap(preCurrentForegroundImageLeft);
                    }
                    if (postCurrentForegroundImageRight != null) {
                        editorForegroundImageRight.setImageBitmap(postCurrentForegroundImageRight);
                    } else {
                        editorForegroundImageRight.setImageBitmap(preCurrentForegroundImageRight);
                    }
                    break;
                case 1:
                    if (postCurrentForegroundImageLeft != null) {
                        editorForegroundImageLeft.setImageBitmap(postCurrentForegroundImageLeft);
                    } else {
                        editorForegroundImageLeft.setImageBitmap(preCurrentForegroundImageLeft);
                    }
                    break;
                case 2:
                    if (postCurrentForegroundImageRight != null) {
                        editorForegroundImageRight.setImageBitmap(postCurrentForegroundImageRight);
                    } else {
                        editorForegroundImageRight.setImageBitmap(preCurrentForegroundImageRight);
                    }
                    break;
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            applyEffect(integers[0].intValue());
            return null;
        }
    }

    private void applyEffect(int whichFilter) {
        switch (imageToApplyEffect) {
            case 0:
                applyEffectOnBoth(whichFilter);
                break;
            case 1:
                applyEffectOnLeftFront(whichFilter);
                break;
            case 2:
                applyEffectOnRightFront(whichFilter);
                break;
        }
    }

    private void applyEffectOnBoth(final int whichFilter) {
        postCurrentForegroundImageLeft = new PipFilters().applyFilter(whichFilter, preCurrentForegroundImageLeft);
        postCurrentForegroundImageRight = new PipFilters().applyFilter(whichFilter, preCurrentForegroundImageRight);
    }

    private void applyEffectOnLeftFront(final int whichFilter) {
        postCurrentForegroundImageLeft = new PipFilters().applyFilter(whichFilter, preCurrentForegroundImageLeft);
    }

    private void applyEffectOnRightFront(final int whichFilter) {
        postCurrentForegroundImageRight = new PipFilters().applyFilter(whichFilter, preCurrentForegroundImageRight);
    }

}
