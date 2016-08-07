package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewClientProfileActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView profilePicture;
    public static boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_profile);

        profilePicture = (ImageView) findViewById(R.id.ivProfileImage);
        Log.i("PROFILE", "CREATE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("PROFILE", "RESUME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    public void onImageGalleryClicked(View view) {
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

                    profilePicture.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Não foi possível carregar a imagem", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void onClickFinish(View view) {

    }

    void onClickBack(View view) {
        Intent i = new Intent(NewClientProfileActivity.this, NewClientMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
