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
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.activity.ServiceProviderDetailsFragment;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteListAdapter extends ArrayAdapter<ServiceProvider> {

    private Resources res;
    private FragmentManager fragmentManager;

    private static class ViewHolder {
        LinearLayout llRow;
        ImageView ivServiceProviderProfile;
        TextView tvServiceProviderName;
        TextView tvExperienceDescription;
        LinearLayout llStars;
    }

    public FavoriteListAdapter(Context context, List<ServiceProvider> serviceProviders, FragmentManager fragmentManager) {
        super(context, 0, serviceProviders);
        this.res = getContext().getResources();
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_list_layout, parent, false);

            viewHolder.llRow = (LinearLayout) convertView.findViewById(R.id.llRow);
            viewHolder.ivServiceProviderProfile = (ImageView) convertView.findViewById(R.id.ivServiceProviderProfile);
            viewHolder.tvServiceProviderName = (TextView) convertView.findViewById(R.id.tvServiceProviderName);
            viewHolder.tvExperienceDescription = (TextView) convertView.findViewById(R.id.tvExperienceDescription);
            viewHolder.llStars = (LinearLayout) convertView.findViewById(R.id.llStars);

            viewHolder.llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    ServiceProvider serviceProvider = getItem(position);
                    arguments.putInt("serviceProviderId", serviceProvider.getId());

                    ServiceProviderDetailsFragment fragment = new ServiceProviderDetailsFragment();
                    fragment.setArguments(arguments);

                    fragmentManager.beginTransaction().replace(R.id.content_default_client, fragment).commit();
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ServiceProvider serviceProvider = getItem(position);

        viewHolder.ivServiceProviderProfile.setImageBitmap(Image.base64ToBitmap(serviceProvider.getProfileImage()));
        viewHolder.tvServiceProviderName.setText(serviceProvider.getName());
        viewHolder.tvExperienceDescription.setText(serviceProvider.getExperienceDescription());
        loadStars(serviceProvider.getId(), convertView);

        return convertView;
    }

    private void loadStars(int id, final View view) {
        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> projectCall = projectService.serviceProviderProjects(id, Project.FINISHED);

        projectCall.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.code() == 200) {
                    List<Project> serviceProviderProjects = response.body();

                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.llStars.addView(getStarsEvaluation(getQualification(serviceProviderProjects)));

                } else {
                    Toast.makeText(getContext(), res.getString(R.string.service_project_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Toast.makeText(getContext(), res.getString(R.string.service_project_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private double getQualification(List<Project> projects){
        double sum = 0.0;

        for (Project project : projects) {
            sum += project.getServiceProviderQualification();
        }

        if(projects.size() > 0) {
            return sum / projects.size();
        }

        return sum;
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
