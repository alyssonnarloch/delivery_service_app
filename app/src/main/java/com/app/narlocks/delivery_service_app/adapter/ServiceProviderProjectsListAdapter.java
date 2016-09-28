package com.app.narlocks.delivery_service_app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity.ClientProjectDetailsFragment;
import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;

import java.util.List;

public class ServiceProviderProjectsListAdapter extends ArrayAdapter<Project> {

    private Resources res;
    private FragmentManager fragmentManager;

    private static class ViewHolder {
        LinearLayout llRow;
        TextView tvTitle;
        TextView tvPeriod;
        RatingBar rbStars;
    }

    public ServiceProviderProjectsListAdapter(Context context, List<Project> projects, FragmentManager fragmentManager) {
        super(context, 0, projects);
        this.res = getContext().getResources();
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_service_provider_project_layout, parent, false);

            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvPeriod = (TextView) convertView.findViewById(R.id.tvPeriod);
            viewHolder.rbStars = (RatingBar) convertView.findViewById(R.id.rbStars);
            viewHolder.llRow = (LinearLayout) convertView.findViewById(R.id.llRow);

            viewHolder.llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    Project project = getItem(position);

                    if(project != null) {
                        arguments.putInt("projectId", project.getId());

                        ClientProjectDetailsFragment fragment = new ClientProjectDetailsFragment();
                        fragment.setArguments(arguments);

                        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();
                    }
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project project = getItem(position);

        viewHolder.tvTitle.setText(project.getTitle());
        viewHolder.tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));

        if(project.getStatus() != null && project.getStatus().getId() == Project.FINISHED) {
            if(project.getServiceProviderQualification() != null) {
                viewHolder.rbStars.setRating(project.getServiceProviderQualification());
            } else {
                viewHolder.rbStars.setRating(0);
            }
        }

        return convertView;
    }
}

