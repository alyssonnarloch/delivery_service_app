package com.app.narlocks.delivery_service_app.activity;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity_task.ClientProjectDetails;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

public class ClientProjectAwaitingFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvServiceProviderName;
    private LinearLayout llStars;
    private TextView tvEvaluation;
    private TextView tvAddress;
    private TextView tvPeriod;
    private TextView tvProjectDescription;
    private Button btFinish;

    private SessionManager session;
    private Resources res;

    public ClientProjectAwaitingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_project_awaiting, container, false);;

        res = getResources();
        session = new SessionManager(getActivity());

        loadViewComponents(view);
        new ClientProjectDetails(this).execute(getArguments().getInt("projectId"));

        return view;
    }

    private void loadViewComponents(View view){
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvServiceProviderName = (TextView) view.findViewById(R.id.tvServiceProviderName);
        llStars = (LinearLayout) view.findViewById(R.id.llStars);
        tvEvaluation = (TextView) view.findViewById(R.id.tvEvaluation);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPeriod = (TextView) view.findViewById(R.id.tvPeriod);
        tvProjectDescription = (TextView) view.findViewById(R.id.tvProjectDescription);
        btFinish = (Button) view.findViewById(R.id.btFinish);
    }

    public void loadContentViewComponents(Project project, List<Project> projects) {
        tvTitle.setText(project.getTitle());
        tvStatus.setText(project.getStatus().getName());
        tvServiceProviderName.setText(project.getServiceProvider().getName());
        tvAddress.setText(project.getAddress() + ", " + project.getNumber() + " - " + project.getCity().getName() + "/" + project.getCity().getState().getName());
        tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));
        tvProjectDescription.setText(project.getDescription());
    }

    private void setStarsEvaluation(double qualification) {
        int intPart = (int) qualification;

        for(int i = 1; i <= intPart; i++) {
            ImageView ivStar = new ImageView(getActivity());

            if(i == intPart && qualification > i && qualification <= (i + 0.9)) {
                ivStar.setImageResource(R.mipmap.ic_star_half_black_24dp);
            } else {
                ivStar.setImageResource(R.mipmap.ic_star_black_24dp);
            }
            llStars.addView(ivStar);
        }

        if(qualification == 0) {
            ImageView ivStar = new ImageView(getActivity());
            ivStar.setImageResource(R.mipmap.ic_star_border_black_24dp);

            llStars.addView(ivStar);
        }
    }
}
