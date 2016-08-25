package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.service.ClientService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewClientProfileActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView ivProfilePicture;
    private Client client;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_profile);

        res = getResources();

        client = (Client) getIntent().getSerializableExtra("clientObj");

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfileImage);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        client = (Client) getIntent().getSerializableExtra("clientObj");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NewClientProfileActivity.this, NewClientMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
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
                    ivProfilePicture.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, res.getString(R.string.image_upload_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onClickFinish(View view) {
        BitmapDrawable drawable = (BitmapDrawable) ivProfilePicture.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String encodedProfilePicture = Image.bitmapToBase64(bitmap);
        client.setProfileImage(encodedProfilePicture);

        save();
    }

    public void backLogin() {
        Intent i = new Intent(NewClientProfileActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void save() {
        ClientService service = ServiceGenerator.createService(ClientService.class);

        Call<ResponseBody> call = service.save(client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getZipCode(),
                client.getCityId(),
                client.getAddress(),
                client.getNumber(),
                client.getPassword(),
                client.getProfileImage());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    Toast.makeText(NewClientProfileActivity.this, ErrorConversor.getErrorMessage(response.errorBody()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewClientProfileActivity.this, res.getString(R.string.client_save_ok), Toast.LENGTH_LONG).show();
                    backLogin();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(NewClientProfileActivity.this, res.getString(R.string.client_save_fail), Toast.LENGTH_LONG).show();
            }
        });
    }
}
