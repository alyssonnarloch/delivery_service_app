package com.app.narlocks.delivery_service_app.adapter;

import android.content.Context;
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
import com.app.narlocks.delivery_service_app.activity.ClientServiceProviderDetailsFragment;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.User;

import java.util.List;

public class EvaluationListAdapter extends ArrayAdapter<Project> {

    private int profileId;
    private FragmentManager fragmentManager;

    private static class ViewHolder {
        LinearLayout llRow;
        ImageView ivProfileImageEvaluation;
        RatingBar rbStars;
        TextView tvNameValuer;
        TextView tvEvaluationDescription;
    }

    public EvaluationListAdapter(Context context, List<Project> projects, int profileId, FragmentManager fragmentManager) {
        super(context, 0, projects);
        this.profileId = profileId;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_evaluation_layout, parent, false);

            viewHolder.llRow = (LinearLayout) convertView.findViewById(R.id.llRow);
            viewHolder.ivProfileImageEvaluation = (ImageView) convertView.findViewById(R.id.ivProfileImageEvaluation);
            viewHolder.rbStars = (RatingBar) convertView.findViewById(R.id.rbStars);
            viewHolder.tvNameValuer = (TextView) convertView.findViewById(R.id.tvNameValuer);
            viewHolder.tvEvaluationDescription = (TextView) convertView.findViewById(R.id.tvEvaluationDescription);

            viewHolder.llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (profileId == User.CLIENT) {
                        Project project = getItem(position);

                        Bundle arguments = new Bundle();
                        arguments.putInt("serviceProviderId", project.getServiceProvider().getId());

                        ClientServiceProviderDetailsFragment fragment = new ClientServiceProviderDetailsFragment();
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

        int qualification = 0;
        String name = "";
        String evaluationDescription = "";
        String profileImage;

        if (profileId == User.CLIENT) {
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
        viewHolder.rbStars.setRating(qualification);
        viewHolder.tvNameValuer.setText(name);
        viewHolder.tvEvaluationDescription.setText(evaluationDescription);

        return convertView;
    }
}
