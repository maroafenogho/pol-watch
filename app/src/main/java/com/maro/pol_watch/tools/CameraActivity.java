package com.maro.pol_watch.tools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.maro.pol_watch.MainActivity;
import com.maro.pol_watch.R;
import com.maro.pol_watch.util.Constants;
import com.maro.pol_watch.util.FetchAddressTask;
import com.maro.pol_watch.util.GpsUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {

    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    private static  final String TRACKING_LOCATION_KEY = "tracking_location";
    private boolean mTrackingLocation;

    TextView locationText;
    private boolean isGPS = false;
    private boolean isContinue = false;
    String photoPath;

    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0;
    private double wayLongitude = 0.0;
    private long timeInMilliseconds;
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION"};

    PreviewView mPreviewView;
    ImageView captureImage;
    ImageCapture imageCapture;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        locationText = findViewById(R.id.textViewLocation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(3000);

        if (savedInstanceState!=null){
            mTrackingLocation = savedInstanceState.getBoolean(TRACKING_LOCATION_KEY);
        }
        new GpsUtils(this).turnOnGps(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (mTrackingLocation){
                    new FetchAddressTask(CameraActivity.this, CameraActivity.this)
                            .execute(locationResult.getLastLocation());
                }
            }
        };

        if (isGPS){
            isContinue=false;
//            getLocation();
            startTrackingLocation();
        }
        mPreviewView = findViewById(R.id.camera);
        captureImage = findViewById(R.id.imgCapture);

        captureImage.setOnClickListener(v -> {
            takePhoto();
            Toast.makeText(CameraActivity.this, "Camera clisked", Toast.LENGTH_SHORT).show();
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    private  void startTrackingLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.LOCATION_REQUEST);
        }else{
            mTrackingLocation = true;
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, null);
            locationText.setText(getString(R.string.address_text, "loading", System.currentTimeMillis()));
        }
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                bindPreview(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
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
            cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       switch (requestCode){
           case 1000: {
               if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                  getLocation();
                   startTrackingLocation();
               }else{
                   Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
               }
               break;
           }
       }

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(this, "Permitted", Toast.LENGTH_SHORT).show();
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by user", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTrackingLocation){
//            getLocation();
            startTrackingLocation();
        }
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        if ( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            settingsRequest();
        }
    }

    private void takePhoto(){
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

//        File file = new File(Environment.get() + "/DCIM/polwatch",mDateFormat.format(new Date())+ ".jpg");
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),mDateFormat.format(new Date())+ ".jpg");

//        Bitmap bitmap = new ;
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                Uri savedUri = Uri.fromFile(file);
                String msg = "photo saved: " + savedUri;
                Toast.makeText(CameraActivity.this, ""+ msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException e) {
                e.printStackTrace();
                Log.i("saveFailed", "onError: "+e.toString());
                Toast.makeText(CameraActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean allPermissionsGranted(){
        for (String permission : REQUIRED_PERMISSIONS){
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    public void settingsRequest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS is disabled. Please enable it to continue")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onTaskCompleted(String result) {
        if (mTrackingLocation){
            locationText.setText(getString(R.string.address_text, result, System.currentTimeMillis()));
        }
    }
}