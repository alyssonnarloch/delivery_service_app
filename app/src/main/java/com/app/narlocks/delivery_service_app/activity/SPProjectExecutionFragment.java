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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity_task.SPProjectExecutionAddImageTask;
import com.app.narlocks.delivery_service_app.activity_task.SPProjectExecutionTask;
import com.app.narlocks.delivery_service_app.adapter.SPProjectPortfolioGridViewAdapter;
import com.app.narlocks.delivery_service_app.adapter.SPProjectPortfolioRemoveGridViewAdapter;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ImageItem;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;
import com.app.narlocks.delivery_service_app.session.SessionManager;
import com.app.narlocks.delivery_service_app.view.ExpandableHeightGridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SPProjectExecutionFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvClientName;
    private TextView tvAddress;
    private TextView tvPeriod;
    private TextView tvProjectDescription;
    private ImageView ivAddImage;
    private ExpandableHeightGridView gvImages;
    private ExpandableHeightGridView gvApprovedImages;
    private Button btFinish;
    private ScrollView svDisplay;

    private Project project;
    private int clientId;

    private SessionManager session;
    private Resources res;

    public static final int IMAGE_GALLERY_REQUEST = 20;

    public SPProjectExecutionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_project_detail);

        this.res = getResources();
        this.session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sp_project_execution, container, false);

        loadViewComponents(view);
        new SPProjectExecutionTask(this).execute(getArguments().getInt("projectId"));

        return view;
    }

    private void loadViewComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvClientName = (TextView) view.findViewById(R.id.tvClientName);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPeriod = (TextView) view.findViewById(R.id.tvPeriod);
        tvProjectDescription = (TextView) view.findViewById(R.id.tvProjectDescription);
        gvImages = (ExpandableHeightGridView) view.findViewById(R.id.gvImages);
        gvImages.setExpanded(true);
        gvApprovedImages = (ExpandableHeightGridView) view.findViewById(R.id.gvApprovedImages);
        gvApprovedImages.setExpanded(true);
        ivAddImage = (ImageView) view.findViewById(R.id.ivAddImage);
        btFinish = (Button) view.findViewById(R.id.btFinish);
        svDisplay = (ScrollView) view.findViewById(R.id.svDisplay);

    }

    private void loadViewListeners() {
        tvClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putInt("clientId", clientId);

                Fragment fragment = new SPClientDetailsFragment();
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_sp_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

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

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("title", project.getTitle());
                arguments.putString("status", project.getStatus().getName());
                arguments.putString("clientName", project.getClient().getName());
                arguments.putInt("projectId", project.getId());
                arguments.putInt("newProjectStatus", Project.FINISHED);

                Fragment fragment = new SPProjectEvaluationFragment();
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_sp_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    public void loadContentViewComponents(Project project) {
        tvTitle.setText(project.getTitle());
        tvStatus.setText(project.getStatus().getName());
        tvClientName.setText(project.getClient().getName());
        tvAddress.setText(project.getAddress() + ", " + project.getNumber() + " (" + project.getZipCode() + ")" + " - " + project.getCity().getName() + "/" + project.getCity().getState().getName());
        tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));
        tvProjectDescription.setText(project.getDescription());

        this.project = project;
        clientId = project.getServiceProvider().getId();

        SPProjectPortfolioRemoveGridViewAdapter notEvaluatedImagesAdapter = new SPProjectPortfolioRemoveGridViewAdapter(getActivity(), R.layout.gridview_image_delete_layout, getNotEvaluated(project.getPortfolio()), getActivity().getSupportFragmentManager());
        gvImages.setAdapter(notEvaluatedImagesAdapter);

        SPProjectPortfolioGridViewAdapter approvedImagesAdapter = new SPProjectPortfolioGridViewAdapter(getActivity(), R.layout.gridview_image_layout, getApprovedImages(project.getPortfolio()), getActivity().getSupportFragmentManager());
        gvApprovedImages.setAdapter(approvedImagesAdapter);

        loadViewListeners();

        svDisplay.smoothScrollTo(0, 0);
    }

    private List<ProjectPortfolio> getNotEvaluated(List<ProjectPortfolio> portfolio) {
        List<ProjectPortfolio> newPortfolio = new ArrayList();

        for (ProjectPortfolio p : portfolio) {
            if (p.isApproved() == null) {
                newPortfolio.add(p);
            }
        }
        return newPortfolio;
    }

    private List<ImageItem> getApprovedImages(List<ProjectPortfolio> portfolio) {
        List<ImageItem> newPortfolio = new ArrayList();

        for (ProjectPortfolio p : portfolio) {
            if (p.isApproved() != null && p.isApproved()) {
                newPortfolio.add(new ImageItem(Image.base64ToBitmap(p.getImage())));
            }
        }
        return newPortfolio;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    //ivAddPicture.setImageBitmap(image);

                    new SPProjectExecutionAddImageTask(this, Image.bitmapToBase64(image)).execute(project.getId());
                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), res.getString(R.string.image_upload_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void realoadGridImages(List<ProjectPortfolio> projectPortfolio) {
        SPProjectPortfolioRemoveGridViewAdapter notEvaluatedImagesAdapter = new SPProjectPortfolioRemoveGridViewAdapter(getActivity(), R.layout.gridview_image_delete_layout, getNotEvaluated(projectPortfolio), getActivity().getSupportFragmentManager());
        gvImages.setAdapter(notEvaluatedImagesAdapter);
        loadViewListeners();
    }

}