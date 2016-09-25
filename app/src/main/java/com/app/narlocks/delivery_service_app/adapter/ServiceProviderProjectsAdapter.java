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
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.activity.SPProjectAwaitingFragment;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;

import java.util.List;

public class ServiceProviderProjectsAdapter extends ArrayAdapter<Project> {

    private Resources res;
    private FragmentManager fragmentManager;

    private static class ViewHolder {
        LinearLayout llRow;
        TextView tvTitle;
        TextView tvClientName;
        TextView tvPeriod;
        ImageView ivProjectStatus;
        RatingBar rbProjectStars;
        int id;
    }

    public ServiceProviderProjectsAdapter(Context context, List<Project> projects, FragmentManager fragmentManager) {
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

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_project_layout, parent, false);

            viewHolder.llRow = (LinearLayout) convertView.findViewById(R.id.llRow);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvClientName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvPeriod = (TextView) convertView.findViewById(R.id.tvPeriod);
            viewHolder.ivProjectStatus = (ImageView) convertView.findViewById(R.id.ivProjectStatus);
            viewHolder.rbProjectStars = (RatingBar) convertView.findViewById(R.id.rbProjectStars);
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
                                SPProjectAwaitingFragment awaitingFragment = new SPProjectAwaitingFragment();
                                awaitingFragment.setArguments(arguments);
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, awaitingFragment).commit();
                            /*case Project.REFUSED:
                                ClientProjectRefusedFragment refusedFragment = new ClientProjectRefusedFragment();
                                refusedFragment.setArguments(arguments);
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, refusedFragment).commit();
                            case Project.EXECUTION:
                                ClientProjectExecutionFragment executionFragment = new ClientProjectExecutionFragment();
                                executionFragment.setArguments(arguments);
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, executionFragment).commit();
                            case Project.FINISHED:
                                ClientProjectFinishedFragment finishedFragment = new ClientProjectFinishedFragment();
                                finishedFragment.setArguments(arguments);
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, finishedFragment).commit();
                                */
                        }
                    }
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(project.getTitle());
        viewHolder.tvClientName.setText(project.getServiceProvider().getName());
        viewHolder.tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));

        if (project.getStatus() != null && (project.getStatus().getId() == Project.FINISHED || project.getStatus().getId() == Project.REFUSED)) {
            viewHolder.rbProjectStars.setRating(project.getServiceProviderQualification());
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
}

