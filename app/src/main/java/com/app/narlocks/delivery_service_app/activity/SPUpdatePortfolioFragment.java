package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity_task.SPUpdatePortfolioLoadTask;
import com.app.narlocks.delivery_service_app.adapter.GridViewPortfolioRemoveAdapter;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ImageItem;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.model.ServiceProviderPortfolio;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SPUpdatePortfolioFragment extends Fragment {

    private ImageView ivAddImage;
    private GridView gvPortfolioImages;
    private Button btUpdate;

    private ServiceProvider serviceProvider;
    private GridViewPortfolioRemoveAdapter gvAdapter;
    private List<ImageItem> imageItems;

    private SessionManager session;
    private Resources res;

    public static final int IMAGE_GALLERY_REQUEST = 20;

    public SPUpdatePortfolioFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.res = getResources();
        this.session = new SessionManager(getActivity());
        this.imageItems = new ArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_service_provider_update);

        View view = inflater.inflate(R.layout.fragment_sp_update_portfolio, container, false);

        loadViewComponents(view);
        loadViewComponentListeners();
        new SPUpdatePortfolioLoadTask(this).execute(session.getUserId());

        return view;
    }

    private void loadViewComponents(View view) {
        ivAddImage = (ImageView) view.findViewById(R.id.ivAddImage);
        gvPortfolioImages = (GridView) view.findViewById(R.id.gvPortfolioImages);
        btUpdate = (Button) view.findViewById(R.id.btUpdate);
    }

    private void loadViewComponentListeners() {
        ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();
                Uri data = Uri.parse(pictureDirectoryPath);

                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    imageItems.add(new ImageItem(image));
                    gvAdapter = new GridViewPortfolioRemoveAdapter(getActivity(), R.layout.gridview_image_delete_layout, imageItems);
                    gvPortfolioImages.setAdapter(gvAdapter);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), res.getString(R.string.image_upload_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void loadContentViewComponents(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;

        for(ServiceProviderPortfolio portfolio : serviceProvider.getPortfolio()) {
            imageItems.add(new ImageItem(Image.base64ToBitmap(portfolio.getImage())));
        }

        gvAdapter = new GridViewPortfolioRemoveAdapter(getActivity(), R.layout.gridview_image_delete_layout, imageItems);
        gvPortfolioImages.setAdapter(gvAdapter);
    }

    private void update() {
        for(ImageItem image : imageItems) {
            serviceProvider.addProfilePortfolioSrc(Image.bitmapToBase64(image.getImage()));
        }
    }

    public void backDashboard() {
        Fragment fragment = new SPDashboardFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_sp_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
