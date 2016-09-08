package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.narlocks.delivery_service_app.extras.Image;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenImageFragment extends Fragment {

    private ImageView ivFullScreen;
    private PhotoViewAttacher mAttacher;

    public FullScreenImageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_screen_image, container, false);

        ivFullScreen = (ImageView) view.findViewById(R.id.ivFullScreen);
        ivFullScreen.setImageBitmap(Image.base64ToBitmap(getArguments().getString("image")));

        mAttacher = new PhotoViewAttacher(ivFullScreen);

        return view;
    }

}
