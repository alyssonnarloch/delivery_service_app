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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.session.SessionManager;

public class SPDashboardFragment extends Fragment {

    private TextView tvServiceProviderName;
    private LinearLayout llSpEdit;
    private LinearLayout llSpServices;
    private LinearLayout llSpOccupationAreas;
    private LinearLayout llSpPortfolio;
    private LinearLayout llSpEvaluations;
    private LinearLayout llSpProjects;

    private SessionManager session;
    private Resources res;

    public SPDashboardFragment() {

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

        getActivity().setTitle(R.string.title_service_provider_update);

        View view = inflater.inflate(R.layout.fragment_sp_dashboard, container, false);

        loadViewComponents(view);
        loadContentViewComponents();
        loadViewListeners();

        return view;
    }

    private void loadViewComponents(View view) {
        tvServiceProviderName = (TextView) view.findViewById(R.id.tvServiceProviderName);
        llSpEdit = (LinearLayout) view.findViewById(R.id.llSpEdit);
        llSpServices = (LinearLayout) view.findViewById(R.id.llSpServices);
        llSpOccupationAreas = (LinearLayout) view.findViewById(R.id.llSpOccupationAreas);
        llSpPortfolio = (LinearLayout) view.findViewById(R.id.llSpPortfolio);
        llSpEvaluations = (LinearLayout) view.findViewById(R.id.llSpEvaluations);
        llSpProjects = (LinearLayout) view.findViewById(R.id.llSpProjects);
    }

    private void loadContentViewComponents() {
        tvServiceProviderName.setText(session.getUserName());
    }

    private void loadViewListeners() {
        llSpEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFragment(new SPUpdateFragment());
            }
        });

        llSpEvaluations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFragment(new SPEvaluationsFragment());
            }
        });

        llSpProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFragment(new SPProjectsFragment());
            }
        });
    }

    private void goToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_sp_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
