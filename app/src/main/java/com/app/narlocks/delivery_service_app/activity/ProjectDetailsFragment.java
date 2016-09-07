package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity_task.ProjectDetailsTask;
import com.app.narlocks.delivery_service_app.adapter.ProjectPortfolioGridViewAdapter;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;

public class ProjectDetailsFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvProjectDescription;
    private TextView tvPeriod;
    private TextView tvEvaluationDescription;
    private LinearLayout llStars;
    private GridView gvImages;

    private int projectId;

    public ProjectDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project_details, container, false);

        projectId = getArguments().getInt("projectId");

        loadViewComponents(view);

        new ProjectDetailsTask(this).execute(projectId);

        return view;
    }

    private void loadViewComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvProjectDescription = (TextView) view.findViewById(R.id.tvProjectDescription);
        tvPeriod = (TextView) view.findViewById(R.id.tvPeriod);
        tvEvaluationDescription = (TextView) view.findViewById(R.id.tvEvaluationDescription);
        llStars = (LinearLayout) view.findViewById(R.id.llStars);
        gvImages = (GridView) view.findViewById(R.id.gvImages);
    }

    public void loadContentViewComponents(Project project) {
        tvTitle.setText(project.getTitle());
        tvProjectDescription.setText(project.getDescription());
        tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));
        tvEvaluationDescription.setText(project.getServiceProviderEvaluation());
        llStars.addView(getStarsEvaluation(project.getServiceProviderQualification()));

        ProjectPortfolioGridViewAdapter adapter = new ProjectPortfolioGridViewAdapter(getActivity(), R.layout.gridview_image_layout, project.getPortfolio());
        gvImages.setAdapter(adapter);
    }

    private LinearLayout getStarsEvaluation(double qualification) {
        LinearLayout llStars = new LinearLayout(getContext());
        int intPart = (int) qualification;

        for(int i = 1; i <= intPart; i++) {
            ImageView ivStar = new ImageView(getContext());

            if(i == intPart && qualification > i && qualification <= (i + 0.9)) {
                ivStar.setImageResource(R.mipmap.ic_star_half_black_24dp);
            } else {
                ivStar.setImageResource(R.mipmap.ic_star_black_24dp);
            }
            llStars.addView(ivStar);
        }

        if(qualification == 0) {
            ImageView ivStar = new ImageView(getContext());
            ivStar.setImageResource(R.mipmap.ic_star_border_black_24dp);

            llStars.addView(ivStar);
        }

        return llStars;
    }
}
