package com.app.narlocks.delivery_service_app.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity.ClientProjectAwaitingFragment;
import com.app.narlocks.delivery_service_app.activity.ClientProjectRefusedFragment;
import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;

import java.util.List;

public class ClientProjectsListAdapter extends ArrayAdapter<Project> {

    private Resources res;
    private FragmentManager fragmentManager;

    private static class ViewHolder {
        LinearLayout llRow;
        TextView tvTitle;
        TextView tvServiceProviderName;
        TextView tvPeriod;
        ImageView ivProjectStatus;
        LinearLayout llProjectStars;
        int id;
    }

    public ClientProjectsListAdapter(Context context, List<Project> projects, FragmentManager fragmentManager) {
        super(context, 0, projects);
        this.res = getContext().getResources();
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        Project project = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_client_project_layout, parent, false);

            viewHolder.llRow = (LinearLayout) convertView.findViewById(R.id.llRow);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvServiceProviderName = (TextView) convertView.findViewById(R.id.tvServiceProviderName);
            viewHolder.tvPeriod = (TextView) convertView.findViewById(R.id.tvPeriod);
            viewHolder.ivProjectStatus = (ImageView) convertView.findViewById(R.id.ivProjectStatus);
            viewHolder.llProjectStars = (LinearLayout) convertView.findViewById(R.id.llProjectStars);
            viewHolder.id = position;

            viewHolder.llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    Project project = getItem(position);

                    if (project != null) {
                        arguments.putInt("projectId", project.getId());

                        switch (project.getStatus().getId()) {
                            case Project.AWATING:
                                ClientProjectAwaitingFragment awaitingFragment = new ClientProjectAwaitingFragment();
                                awaitingFragment.setArguments(arguments);
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, awaitingFragment).commit();
                            case Project.REFUSED:
                                ClientProjectRefusedFragment refusedFragment = new ClientProjectRefusedFragment();
                                refusedFragment.setArguments(arguments);
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, refusedFragment).commit();
                        }
                    }
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(project.getTitle());
        viewHolder.tvServiceProviderName.setText(project.getServiceProvider().getName());
        viewHolder.tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));

        if (project.getStatus() != null && (project.getStatus().getId() == Project.FINISHED || project.getStatus().getId() == Project.REFUSED)) {
            viewHolder.llProjectStars.removeAllViews();
            viewHolder.llProjectStars.addView(getStarsEvaluation(project.getServiceProviderQualification()));
        }

        int projectStatusId = R.mipmap.ic_schedule_black_24dp;

        switch (project.getStatus().getId()) {
            case 2:
                projectStatusId = R.mipmap.ic_block_black_24dp;
                break;
            case 3:
                projectStatusId = R.mipmap.ic_hourglass_empty_black_24dp;
                break;
            case 4:
                projectStatusId = R.mipmap.ic_hourglass_full_black_24dp;
                break;
        }

        viewHolder.ivProjectStatus.setImageResource(projectStatusId);

        return convertView;
    }

    private LinearLayout getStarsEvaluation(double qualification) {
        LinearLayout llStars = new LinearLayout(getContext());
        int intPart = (int) qualification;

        for (int i = 1; i <= intPart; i++) {
            ImageView ivStar = new ImageView(getContext());

            if (i == intPart && qualification > i && qualification <= (i + 0.9)) {
                ivStar.setImageResource(R.mipmap.ic_star_half_black_24dp);
            } else {
                ivStar.setImageResource(R.mipmap.ic_star_black_24dp);
            }
            llStars.addView(ivStar);
        }

        if (qualification == 0) {
            ImageView ivStar = new ImageView(getContext());
            ivStar.setImageResource(R.mipmap.ic_star_border_black_24dp);

            llStars.addView(ivStar);
        }

        return llStars;
    }
}
