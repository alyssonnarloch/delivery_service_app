package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewClientProfileActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView ivProfilePicture;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_profile);

        client = (Client) getIntent().getSerializableExtra("clientObj");

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfileImage);
        Log.i("PROFILE", "CREATE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("PROFILE", "RESUME");
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

                    /** TESTE MAROTO
                     String encoded = Image.bitmapToBase64(image);
                     Bitmap decodedImage = Image.base64ToBitmap(encoded);
                     ivProfilePicture.setImageBitmap(decodedImage);
                     //Log.i("##### BASE 64 #####", Image.bitmapToBase64(image));
                     */

                    ivProfilePicture.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Não foi possível carregar a imagem", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onClickFinish(View view) {
        BitmapDrawable drawable = (BitmapDrawable) ivProfilePicture.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String encodedImage = Image.bitmapToBase64(bitmap);
    }

    public void onClickBack(View view) {
        Intent i = new Intent(NewClientProfileActivity.this, NewClientMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
