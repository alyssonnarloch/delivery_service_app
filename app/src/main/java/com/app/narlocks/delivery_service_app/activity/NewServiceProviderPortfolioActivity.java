package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.adapter.GridViewPortfolioAdapter;
import com.app.narlocks.delivery_service_app.model.ImageItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NewServiceProviderPortfolioActivity extends AppCompatActivity {

    private ImageButton ibNewImage;
    private GridView gvPortfolioImages;
    private GridViewPortfolioAdapter gvAdapter;
    private Resources res;
    private List<ImageItem> imageItems;
    public static final int IMAGE_GALLERY_REQUEST = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_provider_portfolio);

        res = getResources();

        imageItems = new ArrayList();

        gvAdapter = new GridViewPortfolioAdapter(this, R.layout.image_layout, imageItems);
        gvPortfolioImages = (GridView) findViewById(R.id.gvPortfolioImages);
        gvPortfolioImages.setAdapter(gvAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickNewImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    ImageItem imageItem = new ImageItem(image);

                    imageItems.add(imageItem);
                    gvAdapter = new GridViewPortfolioAdapter(this, R.layout.image_layout, imageItems);
                    gvPortfolioImages.setAdapter(gvAdapter);

                    //ivProfilePicture.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, res.getString(R.string.image_upload_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
