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
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity_task.ClientProjectDetailsTask;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

public class ClientProjectAwaitingFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvServiceProviderName;
    private TextView tvEvaluation;
    private TextView tvAddress;
    private TextView tvPeriod;
    private TextView tvProjectDescription;
    private Button btFinish;

    private int serviceProviderId;

    private SessionManager session;
    private Resources res;

    public ClientProjectAwaitingFragment() {

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
        View view = inflater.inflate(R.layout.fragment_client_project_awaiting, container, false);

        loadViewComponents(view);
        new ClientProjectDetailsTask(this).execute(getArguments().getInt("projectId"));

        return view;
    }

    private void loadViewComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvServiceProviderName = (TextView) view.findViewById(R.id.tvServiceProviderName);
        tvEvaluation = (TextView) view.findViewById(R.id.tvEvaluation);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPeriod = (TextView) view.findViewById(R.id.tvPeriod);
        tvProjectDescription = (TextView) view.findViewById(R.id.tvProjectDescription);
        btFinish = (Button) view.findViewById(R.id.btFinish);
    }

    private void loadViewListeners() {
        tvServiceProviderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putInt("serviceProviderId", serviceProviderId);

                Fragment fragment = new ServiceProviderDetailsFragment();
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
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

        serviceProviderId = project.getServiceProvider().getId();

        loadViewListeners();
    }
}
