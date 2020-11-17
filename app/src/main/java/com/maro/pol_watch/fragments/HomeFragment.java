package com.maro.pol_watch.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maro.pol_watch.MainActivity;
import com.maro.pol_watch.R;
import com.maro.pol_watch.tools.CameraActivity;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    ImageView takePhoto, takeVideo, viewPhotos, viewVideos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        takePhoto = view.findViewById(R.id.imgTakePhoto);
        takeVideo = view.findViewById(R.id.imgRecordVideo);
        viewPhotos = view.findViewById(R.id.imgPhotoLibrary);
        viewVideos = view.findViewById(R.id.imgVideoLibrary);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CameraActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}