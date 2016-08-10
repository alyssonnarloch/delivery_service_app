package com.app.narlocks.delivery_service_app.async_task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageTask extends AsyncTask<Void, Void, String> {
    private Bitmap image;
    private String name;

    public ImageTask(Bitmap image, String name) {
        this.image = image;
        this.name = name;
    }

    @Override
    protected String doInBackground(Void... params) {
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.NO_WRAP);

        Log.i("%%% TESTEEE %%%", temp);

        return null;
    }
}
