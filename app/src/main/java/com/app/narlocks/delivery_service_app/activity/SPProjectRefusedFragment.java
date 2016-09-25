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
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity_task.SPProjectRefusedTask;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.session.SessionManager;

public class SPProjectRefusedFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvClientName;
    private TextView tvAddress;
    private TextView tvPeriod;
    private TextView tvProjectDescription;
    private TextView tvClientEvaluation;
    private RatingBar rbClientEvaluation;
    private TextView tvServiceProviderEvaluation;
    private RatingBar rbServiceProviderEvaluation;

    private Project project;
    private int clientId;

    private SessionManager session;
    private Resources res;

    public SPProjectRefusedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.res = getResources();
        this.session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_project_detail);

        View view = inflater.inflate(R.layout.fragment_sp_project_refused, container, false);

        loadViewComponents(view);
        new SPProjectRefusedTask(this).execute(getArguments().getInt("projectId"));

        return view;
    }

    private void loadViewComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvClientName = (TextView) view.findViewById(R.id.tvClientName);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPeriod = (TextView) view.findViewById(R.id.tvPeriod);
        tvProjectDescription = (TextView) view.findViewById(R.id.tvProjectDescription);
        tvClientEvaluation = (TextView) view.findViewById(R.id.tvClientEvaluation);
        rbClientEvaluation = (RatingBar) view.findViewById(R.id.rbClientEvaluation);
        tvServiceProviderEvaluation = (TextView) view.findViewById(R.id.tvServiceProviderEvaluation);
        rbServiceProviderEvaluation = (RatingBar) view.findViewById(R.id.rbServiceProviderEvaluation);
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

    }

    public void loadContentViewComponents(Project project) {
        tvTitle.setText(project.getTitle());
        tvStatus.setText(project.getStatus().getName());
        tvClientName.setText(project.getClient().getName());
        tvAddress.setText(project.getAddress() + ", " + project.getNumber() + " (" + project.getZipCode() + ")" + " - " + project.getCity().getName() + "/" + project.getCity().getState().getName());
        tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));
        tvProjectDescription.setText(project.getDescription());
        tvClientEvaluation.setText(project.getClientEvaluation());
        rbClientEvaluation.setRating((int) project.getClientQualification());
        tvServiceProviderEvaluation.setText(project.getServiceProviderEvaluation());
        rbServiceProviderEvaluation.setRating((int) (project.getServiceProviderQualification() == null ? 0 : project.getServiceProviderQualification()));

        this.project = project;
        clientId = project.getClient().getId();

        loadViewListeners();
    }

}
