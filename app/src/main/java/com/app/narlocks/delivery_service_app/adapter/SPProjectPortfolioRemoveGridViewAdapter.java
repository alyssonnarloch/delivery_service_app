package com.app.narlocks.delivery_service_app.adapter;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.narlocks.delivery_service_app.activity.FullScreenImageFragment;
import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.activity_task.SPProjectExecutionDeleteImageTask;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;

import java.util.List;

public class SPProjectPortfolioRemoveGridViewAdapter extends ArrayAdapter {

    static class ViewHolder {
        ImageView image;
        ImageButton removeButton;
    }

    private Context context;
    private int layoutResourceId;
    private List<ProjectPortfolio> portfolio;
    private FragmentManager fragmentManager;

    public SPProjectPortfolioRemoveGridViewAdapter(Context context, int layoutResourceId, List<ProjectPortfolio> portfolio, FragmentManager fragmentManager) {
        super(context, layoutResourceId, portfolio);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.portfolio = portfolio;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.ivThumbnailPortfolio);
            holder.removeButton = (ImageButton) row.findViewById(R.id.ibRemoveImage);
            holder.removeButton.setTag(position);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putString("image", portfolio.get(position).getImage());

                    FullScreenImageFragment fragment = new FullScreenImageFragment();
                    fragment.setArguments(arguments);

                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();
                }
            });

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    new SPProjectExecutionDeleteImageTask(context).execute(portfolio.get(position).getId());
                    portfolio.remove(position);
                    notifyDataSetChanged();
                }
            });

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ProjectPortfolio projectPortfolio = (ProjectPortfolio) portfolio.get(position);
        holder.image.setImageBitmap(Image.base64ToBitmap(projectPortfolio.getImage()));

        return row;
    }
}

