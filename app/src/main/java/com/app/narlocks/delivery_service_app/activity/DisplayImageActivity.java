package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.app.narlocks.delivery_service_app.extras.Image;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView ivPortfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        String imageBase64 = (String) getIntent().getSerializableExtra("imageBase64");

        ivPortfolio = (ImageView) findViewById(R.id.ivPortfolio);
        ivPortfolio.setImageBitmap(Image.base64ToBitmap(imageBase64));
    }
}
