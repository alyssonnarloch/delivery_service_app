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

import com.app.narlocks.delivery_service_app.adapter.GridViewPortfolioRemoveAdapter;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ImageItem;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewServiceProviderPortfolioActivity extends AppCompatActivity {

    private ImageButton ibNewImage;
    private GridView gvPortfolioImages;
    private GridViewPortfolioRemoveAdapter gvAdapter;

    private Resources res;
    private List<ImageItem> imageItems;

    private ServiceProvider serviceProvider;
    public static final int IMAGE_GALLERY_REQUEST = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_provider_portfolio);

        res = getResources();
        this.serviceProvider = (ServiceProvider) getIntent().getSerializableExtra("serviceProviderObj");

        imageItems = new ArrayList();

        gvAdapter = new GridViewPortfolioRemoveAdapter(this, R.layout.gridview_image_delete_layout, imageItems);
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
        serviceProvider = (ServiceProvider) getIntent().getSerializableExtra("serviceProviderObj");
        serviceProvider.setProfilePortfolioSrc(new ArrayList());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NewServiceProviderPortfolioActivity.this, NewServiceProviderAreasActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra("serviceProviderObj", serviceProvider);
        startActivity(i);
    }

    public void onClickNewImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    public void onClickNext(View view) {
        if (validate()) {
            for (ImageItem imageItem : imageItems) {
                serviceProvider.addProfilePortfolioSrc(Image.bitmapToBase64(imageItem.getImage()));
            }

            save();
        } else {
            Toast.makeText(NewServiceProviderPortfolioActivity.this, res.getString(R.string.portfolio_required), Toast.LENGTH_LONG).show();
        }
    }

    public void backLogin() {
        Intent i = new Intent(NewServiceProviderPortfolioActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
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
                    gvAdapter = new GridViewPortfolioRemoveAdapter(this, R.layout.gridview_image_delete_layout, imageItems);
                    gvPortfolioImages.setAdapter(gvAdapter);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, res.getString(R.string.image_upload_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean validate() {
        boolean isValid = true;

        if (imageItems.size() == 0) {
            isValid = false;
        }

        return isValid;
    }

    private void save() {
        ServiceProviderService service = ServiceGenerator.createService(ServiceProviderService.class);

        Call<ResponseBody> call = service.save(serviceProvider.getName(),
                serviceProvider.getEmail(),
                serviceProvider.getPhone(),
                serviceProvider.getZipCode(),
                serviceProvider.getCityId(),
                serviceProvider.getAddress(),
                serviceProvider.getNumber(),
                Extra.getMD5(serviceProvider.getPassword()),
                serviceProvider.getProfileImage(),
                serviceProvider.getServiceTypeIds(),
                serviceProvider.getExperienceDescription(),
                serviceProvider.isAvailable(),
                serviceProvider.getOccupationAreaIds(),
                serviceProvider.getProfilePortfolioSrc());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    Toast.makeText(NewServiceProviderPortfolioActivity.this, ErrorConversor.getErrorMessage(response.errorBody()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewServiceProviderPortfolioActivity.this, res.getString(R.string.client_save_ok), Toast.LENGTH_LONG).show();
                    backLogin();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(NewServiceProviderPortfolioActivity.this, res.getString(R.string.service_provider_save_fail), Toast.LENGTH_LONG).show();
            }
        });
    }
}
