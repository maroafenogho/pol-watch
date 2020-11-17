package com.maro.pol_watch.tools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.maro.pol_watch.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS ={"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION"};

    PreviewView mPreviewView;
    ImageView captureImage;
    ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPreviewView = findViewById(R.id.camera);
        captureImage = findViewById(R.id.imgCapture);
//        if (allPermissionsGranted()){
//            startCamera();
//        } else{
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//        }

    }

    private void startCamera(){
        final ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                    bindPreview(cameraProvider);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
        ImageCapture.Builder builder = new ImageCapture.Builder();

        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

         imageCapture = builder.setTargetRotation(this
                .getWindowManager()
                .getDefaultDisplay()
                .getRotation())
                .build();

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle((LifecycleOwner)this,cameraSelector,preview,imageAnalysis,imageCapture);
        } catch (Exception e) {
            e.printStackTrace();
        }


        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                Toast.makeText(CameraActivity.this, "Camera clisked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by user", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    public  String getBatchDirectoryName(){
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageState().toString() + "/images";
        File dir = new File(app_folder_path);
        if(!dir.exists() && !dir.mkdirs()){
        }
        return app_folder_path;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void takePhoto(){
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

//        File file = new File(Environment.get() + "/DCIM/polwatch",mDateFormat.format(new Date())+ ".jpg");
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),mDateFormat.format(new Date())+ ".jpg");

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                Uri savedUri = Uri.fromFile(file);
                String msg = "photo saved: " + savedUri;
                Toast.makeText(CameraActivity.this, ""+ msg, Toast.LENGTH_SHORT).show();
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(CameraActivity.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException e) {
                e.printStackTrace();
                Log.i("saveFailed", "onError: "+e.toString());
                Toast.makeText(CameraActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String locationString(final Location location){
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " " + Location.convert(location.getLatitude(), Location.FORMAT_DEGREES);
    }

    private boolean allPermissionsGranted(){
        for (String permission : REQUIRED_PERMISSIONS){
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}