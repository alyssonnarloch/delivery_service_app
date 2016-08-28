package com.app.narlocks.delivery_service_app.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Project;

import java.util.List;

public class ProjectListAdapter extends ArrayAdapter<Project> {

    private static class ViewHolder {
        ImageView ivProfileImageEvaluation;
        LinearLayout llStars;
        TextView tvNameValuer;
        TextView tvEvaluationDescription;
    }

    public ProjectListAdapter(Context contex, List<Project> projects) {
        super(contex, 0, projects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.project_list_layout, parent, false);

            viewHolder.ivProfileImageEvaluation = (ImageView) convertView.findViewById(R.id.ivProfileImageEvaluation);
            viewHolder.llStars = (LinearLayout) convertView.findViewById(R.id.llStars);
            viewHolder.tvNameValuer = (TextView) convertView.findViewById(R.id.tvNameValuer);
            viewHolder.tvEvaluationDescription = (TextView) convertView.findViewById(R.id.tvEvaluationDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project project = getItem(position);

        viewHolder.ivProfileImageEvaluation.setImageBitmap(Image.base64ToBitmap(project.getServiceProvider().getProfileImage()));
        viewHolder.llStars.addView(getStarsEvaluation(project.getClientQualification()));
        viewHolder.tvNameValuer.setText(project.getServiceProvider().getName());
        viewHolder.tvEvaluationDescription.setText(project.getClientEvaluation());

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
