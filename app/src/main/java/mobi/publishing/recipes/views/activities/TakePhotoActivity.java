package mobi.publishing.recipes.views.activities;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.helpers.ImageSurfaceView;
import mobi.publishing.recipes.helpers.RecognizeImage;

public class TakePhotoActivity extends AppCompatActivity {
    private ImageSurfaceView mImageSurfaceView;
    private Camera camera;
    private FrameLayout cameraPreviewLayout;

    private FloatingActionButton fabCapture;
    private File imageTaken;
    private ArrayList<String> currentIngredients;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraPreviewLayout = findViewById(R.id.frameLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupCamera();

        currentIngredients = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("currentIngredients")) {
                currentIngredients = extras.getStringArrayList("currentIngredients");
            }
        }

        fabCapture = findViewById(R.id.fab);

        fabCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabCapture.getTag().equals("active")) {
                    camera.takePicture(null, null, pictureCallback);
                } else if(fabCapture.getTag().equals("captured")) {
                    new RecognizeImage(TakePhotoActivity.this, imageTaken.getAbsolutePath(), currentIngredients)
                            .execute();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupCamera() {
        camera = checkDeviceCamera();
        mImageSurfaceView = new ImageSurfaceView(this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);
    }

    private Camera checkDeviceCamera(){
        Camera mCamera = null;
        try {
            mCamera = Camera.open();

            //set camera to continually auto-focus
            Camera.Parameters params = mCamera.getParameters();
            //*EDIT*//params.setFocusMode("continuous-picture");
            //It is better to use defined constraints as opposed to String, thanks to AbdelHady
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            imageTaken = pictureFile;
            fabCapture.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            fabCapture.setImageDrawable(ContextCompat.getDrawable(TakePhotoActivity.this,
                    R.drawable.ic_done_white_24dp));
            fabCapture.setTag("captured");
            menu.findItem(R.id.action_undo).setVisible(true);
        }
    };

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_take_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            /*case R.id.action_confirm:
                if(imageTaken == null) {
                    Toast.makeText(this, "No image taken. Please try again.", Toast.LENGTH_LONG).show();
                    return true;
                }

                new RecognizeImage(TakePhotoActivity.this, imageTaken.getAbsolutePath(), currentIngredients)
                        .execute();

                return true;*/
            case R.id.action_undo:
                if(imageTaken == null) {
                    Toast.makeText(this, "No image taken. Please try again.", Toast.LENGTH_LONG).show();
                    return true;
                }

                imageTaken = null;
                cameraPreviewLayout.removeView(mImageSurfaceView);
                setupCamera();

                fabCapture.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                fabCapture.setImageDrawable(ContextCompat.getDrawable(TakePhotoActivity.this,
                        R.drawable.baseline_add_a_photo_white_24));
                fabCapture.setTag("active");
                menu.findItem(R.id.action_undo).setVisible(false);
                return true;
            default:
                return true;
        }
    }
}
