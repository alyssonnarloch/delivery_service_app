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
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity_task.ClientProjectRefusedTask;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

public class ClientProjectRefusedFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvServiceProviderName;
    private TextView tvAddress;
    private TextView tvPeriod;
    private TextView tvProjectDescription;
    private TextView tvClientEvaluation;
    private RatingBar rbClientEvaluation;
    private TextView tvServiceProviderEvaluation;
    private RatingBar rbServiceProviderEvaluation;
    private Button btFinish;

    private Project project;
    private int serviceProviderId;

    private SessionManager session;
    private Resources res;

    public ClientProjectRefusedFragment() {

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

        View view = inflater.inflate(R.layout.fragment_client_project_resfused, container, false);

        loadViewComponents(view);
        new ClientProjectRefusedTask(this).execute(getArguments().getInt("projectId"));

        return view;
    }

    private void loadViewComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvServiceProviderName = (TextView) view.findViewById(R.id.tvServiceProviderName);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPeriod = (TextView) view.findViewById(R.id.tvPeriod);
        tvProjectDescription = (TextView) view.findViewById(R.id.tvProjectDescription);
        tvClientEvaluation = (TextView) view.findViewById(R.id.tvClientEvaluation);
        rbClientEvaluation = (RatingBar) view.findViewById(R.id.rbClientEvaluation);
        tvServiceProviderEvaluation = (TextView) view.findViewById(R.id.tvServiceProviderEvaluation);
        rbServiceProviderEvaluation = (RatingBar) view.findViewById(R.id.rbServiceProviderEvaluation);
        btFinish = (Button) view.findViewById(R.id.btFinish);
    }

    private void loadViewListeners() {
        tvServiceProviderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putInt("serviceProviderId", serviceProviderId);

                Fragment fragment = new ClientServiceProviderDetailsFragment();
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("title", project.getTitle());
                arguments.putString("status", project.getStatus().getName());
                arguments.putString("serviceProviderName", project.getServiceProvider().getName());
                arguments.putInt("projectId", project.getId());
                arguments.putInt("newProjectStatus", Project.REFUSED);

                Fragment fragment = new ClientProjectEvaluationFragment();
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    public void loadContentViewComponents(Project project, List<Project> projects) {
        tvTitle.setText(project.getTitle());
        tvStatus.setText(project.getStatus().getName());
        tvServiceProviderName.setText(project.getServiceProvider().getName());
        tvAddress.setText(project.getAddress() + ", " + project.getNumber() + " (" + project.getZipCode() + ")" + " - " + project.getCity().getName() + "/" + project.getCity().getState().getName());
        tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));
        tvProjectDescription.setText(project.getDescription());
        tvClientEvaluation.setText(project.getClientEvaluation());
        rbClientEvaluation.setRating((int) project.getClientQualification());
        tvServiceProviderEvaluation.setText(project.getServiceProviderEvaluation());
        rbServiceProviderEvaluation.setRating((int) (project.getServiceProviderQualification() == null ? 0 : project.getServiceProviderQualification()));

        if(project.getServiceProviderEvaluation() != null && !project.getServiceProviderEvaluation().isEmpty()) {
            btFinish.setVisibility(View.INVISIBLE);
        }

        this.project = project;
        serviceProviderId = project.getServiceProvider().getId();

        loadViewListeners();
    }

}
