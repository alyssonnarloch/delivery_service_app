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

import com.app.narlocks.delivery_service_app.activity_task.SPProjectExecutionTask;
import com.app.narlocks.delivery_service_app.adapter.ImagesToApproveGridViewAdapter;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;
import com.app.narlocks.delivery_service_app.session.SessionManager;
import com.app.narlocks.delivery_service_app.view.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.List;

public class SPProjectExecutionFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvClientName;
    private TextView tvAddress;
    private TextView tvPeriod;
    private TextView tvProjectDescription;
    private ExpandableHeightGridView gvImages;
    private Button btFinish;

    private Project project;
    private int clientId;

    private SessionManager session;
    private Resources res;

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
        btFinish = (Button) view.findViewById(R.id.btFinish);
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

                Fragment fragment = new ClientProjectEvaluationFragment();
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

        ImagesToApproveGridViewAdapter adapter = new ImagesToApproveGridViewAdapter(getActivity(), R.layout.gridview_image_approve_layout, getNotEvaluated(project.getPortfolio()), getActivity().getSupportFragmentManager());
        gvImages.setAdapter(adapter);

        loadViewListeners();
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
}