package com.maro.pol_watch.tools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.maro.pol_watch.R;

public class VideoCapture extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG = "VIDEO CAPTURE";
    private MediaRecorder mediaRecorder;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Camera camera;
    private Button toggleButton;
    private boolean mInitSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_capture);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        surfaceView = findViewById(R.id.videoCamView);
        holder = surfaceView.getHolder();
        holder.addCallback(this);

        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.getText().equals("DD")){
                    mediaRecorder.start();
                    toggleButton.setText("AA");
                } else{
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    toggleButton.setText("DD");
                    try {
                        initRecorder(holder.getSurface());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initRecorder(Surface surface) {
        if (camera == null){

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}