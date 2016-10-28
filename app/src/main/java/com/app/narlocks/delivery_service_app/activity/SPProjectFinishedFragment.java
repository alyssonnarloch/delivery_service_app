package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity_task.SPProjectFinishedTask;
import com.app.narlocks.delivery_service_app.adapter.SPProjectPortfolioGridViewAdapter;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ImageItem;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;
import com.app.narlocks.delivery_service_app.session.SessionManager;
import com.app.narlocks.delivery_service_app.view.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.List;

public class SPProjectFinishedFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvClientName;
    private RatingBar rbClientEvaluation;
    private RatingBar rbServiceProviderEvaluation;
    private TextView tvAddress;
    private TextView tvPeriod;
    private TextView tvProjectDescription;
    private ExpandableHeightGridView gvApprovedImages;
    private Button btFinish;
    private ScrollView svDisplay;

    private Project project;
    private int clientId;

    private SessionManager session;
    private Resources res;

    public SPProjectFinishedFragment() {

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

        View view = inflater.inflate(R.layout.fragment_sp_project_finished, container, false);

        loadViewComponents(view);
        new SPProjectFinishedTask(this).execute(getArguments().getInt("projectId"));

        return view;
    }

    private void loadViewComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvClientName = (TextView) view.findViewById(R.id.tvClientName);
        rbClientEvaluation = (RatingBar) view.findViewById(R.id.rbClientEvaluation);
        rbServiceProviderEvaluation = (RatingBar) view.findViewById(R.id.rbServiceProviderEvaluation);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPeriod = (TextView) view.findViewById(R.id.tvPeriod);
        tvProjectDescription = (TextView) view.findViewById(R.id.tvProjectDescription);
        gvApprovedImages = (ExpandableHeightGridView) view.findViewById(R.id.gvApprovedImages);
        gvApprovedImages.setExpanded(true);
        btFinish = (Button) view.findViewById(R.id.btFinish);
        btFinish.setVisibility(View.INVISIBLE);
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
        rbClientEvaluation.setRating((project.getClientQualification() == null ? 0 : project.getClientQualification()));
        rbServiceProviderEvaluation.setRating((project.getServiceProviderQualification() == null ? 0 : project.getServiceProviderQualification()));

        this.project = project;
        clientId = project.getClient().getId();

        SPProjectPortfolioGridViewAdapter approvedImagesAdapter = new SPProjectPortfolioGridViewAdapter(getActivity(), R.layout.gridview_image_layout, getApprovedImages(project.getPortfolio()), getActivity().getSupportFragmentManager());
        gvApprovedImages.setAdapter(approvedImagesAdapter);

        loadViewListeners();

        if(project.getClientQualification() == null) {
            btFinish.setVisibility(View.VISIBLE);
        }

        svDisplay.smoothScrollTo(0, 0);
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

}
