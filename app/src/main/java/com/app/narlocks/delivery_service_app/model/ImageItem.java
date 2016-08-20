package com.app.narlocks.delivery_service_app.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImageItem implements Serializable{

    private Bitmap image;

    public ImageItem(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
