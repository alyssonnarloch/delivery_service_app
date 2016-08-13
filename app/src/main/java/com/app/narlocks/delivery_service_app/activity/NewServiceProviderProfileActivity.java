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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewServiceProviderProfileActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView ivProfilePicture;
    private ServiceProvider serviceProvider;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_provider_profile);

        res = getResources();

        serviceProvider = (ServiceProvider) getIntent().getSerializableExtra("serviceProviderObj");

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfileImage);
        Log.i("PROFILE", "CREATE");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("PROFILE", "NEW INTENT");
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        serviceProvider = (ServiceProvider) getIntent().getSerializableExtra("serviceProviderObj");

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
                    Toast.makeText(this, res.getString(R.string.image_upload_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onClickFinish(View view) {
        BitmapDrawable drawable = (BitmapDrawable) ivProfilePicture.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String encodedProfilePicture = Image.bitmapToBase64(bitmap);
        serviceProvider.setProfileImage(encodedProfilePicture);

        save();
    }

    public void onClickBack(View view) {
        Intent i = new Intent(NewServiceProviderProfileActivity.this, NewServiceProviderMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    public void backLogin() {
        Intent i = new Intent(NewServiceProviderProfileActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    public void save() {
        ServiceProviderService service = ServiceGenerator.createService(ServiceProviderService.class);

        /*
        Call<ResponseBody> call = service.save(serviceProvider.getName(),
                serviceProvider.getEmail(),
                serviceProvider.getPhone(),
                serviceProvider.getZipCode(),
                serviceProvider.getCityId(),
                serviceProvider.getAddress(),
                serviceProvider.getNumber(),
                serviceProvider.getPassword(),
                serviceProvider.getProfileImage());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    Toast.makeText(NewServiceProviderProfileActivity.this, ErrorConversor.getErrorMessage(response.errorBody()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewServiceProviderProfileActivity.this, res.getString(R.string.service_provider_save_ok), Toast.LENGTH_LONG).show();
                    backLogin();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(NewServiceProviderProfileActivity.this, res.getString(R.string.service_provider_save_fail), Toast.LENGTH_LONG).show();
            }
        });
        */
    }
}
