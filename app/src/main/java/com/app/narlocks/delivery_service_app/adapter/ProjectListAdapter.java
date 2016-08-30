package com.app.narlocks.delivery_service_app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        LinearLayout llStars;
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
            viewHolder.llStars = (LinearLayout) convertView.findViewById(R.id.llStars);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project project = getItem(position);

        viewHolder.tvTitle.setText(project.getTitle());
        viewHolder.tvServiceProviderName.setText(project.getServiceProvider().getName());
        viewHolder.tvPeriod.setText(Extra.dateToString(project.getStartAt(), "dd/MM/yyyy") + " - " + Extra.dateToString(project.getEndAt(), "dd/MM/yyyy"));

        if(project.getStatus() != null && project.getStatus().getId() == Project.FINISHED) {
            viewHolder.llStars.addView(getStarsEvaluation(project.getServiceProviderQualification()));
        }

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
