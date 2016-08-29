package com.app.narlocks.delivery_service_app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.Extra;
import com.app.narlocks.delivery_service_app.model.Project;

import java.util.List;

public class ProjectListAdapter extends ArrayAdapter<Project> {

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvServiceProviderName;
        TextView tvPeriod;
        ImageView ivProjectStatus;
    }

    public ProjectListAdapter(Context context, List<Project> projects) {
        super(context, 0, projects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.project_list_layout, parent, false);

            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvServiceProviderName = (TextView) convertView.findViewById(R.id.tvServiceProviderName);
            viewHolder.tvPeriod = (TextView) convertView.findViewById(R.id.tvPeriod);
            viewHolder.ivProjectStatus = (ImageView) convertView.findViewById(R.id.ivProjectStatus);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project project = getItem(position);

        viewHolder.tvTitle.setText(project.getTitle());
        viewHolder.tvServiceProviderName.setText(project.getServiceProvider().getName());
        viewHolder.tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));

        int projectStatusId = R.mipmap.ic_schedule_black_24dp;

        switch(project.getStatus().getId()){
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
