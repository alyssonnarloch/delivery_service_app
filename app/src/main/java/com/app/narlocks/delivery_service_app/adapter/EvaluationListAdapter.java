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
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.User;

import java.util.List;

public class EvaluationListAdapter extends ArrayAdapter<Project> {

    private int profileId;

    private static class ViewHolder {
        ImageView ivProfileImageEvaluation;
        LinearLayout llStars;
        TextView tvNameValuer;
        TextView tvEvaluationDescription;
    }

    public EvaluationListAdapter(Context context, List<Project> projects, int profileId) {
        super(context, 0, projects);
        this.profileId = profileId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_evaluation_layout, parent, false);

            viewHolder.ivProfileImageEvaluation = (ImageView) convertView.findViewById(R.id.ivProfileImageEvaluation);
            viewHolder.llStars = (LinearLayout) convertView.findViewById(R.id.llStars);
            viewHolder.tvNameValuer = (TextView) convertView.findViewById(R.id.tvNameValuer);
            viewHolder.tvEvaluationDescription = (TextView) convertView.findViewById(R.id.tvEvaluationDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project project = getItem(position);

        int qualification = 0;
        String name = "";
        String evaluationDescription = "";
        String profileImage;

        if(profileId == User.CLIENT) {
            qualification = project.getClientQualification();
            name = project.getServiceProvider().getName();
            evaluationDescription = project.getClientEvaluation();
            profileImage = project.getServiceProvider().getProfileImage();
        } else {
            qualification = project.getServiceProviderQualification();
            name = project.getClient().getName();
            evaluationDescription = project.getServiceProviderEvaluation();
            profileImage = project.getClient().getProfileImage();
        }

        viewHolder.ivProfileImageEvaluation.setImageBitmap(Image.base64ToBitmap(profileImage));
        viewHolder.llStars.removeAllViews();
        viewHolder.llStars.addView(getStarsEvaluation(qualification));
        viewHolder.tvNameValuer.setText(name);
        viewHolder.tvEvaluationDescription.setText(evaluationDescription);

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
