package com.app.narlocks.delivery_service_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.app.narlocks.delivery_service_app.activity.FullScreenImageFragment;
import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;

import java.util.List;

public class ProjectPortfolioGridViewAdapter extends ArrayAdapter {

    static class ViewHolder {
        ImageView image;
    }

    private Context context;
    private int layoutResourceId;
    private List<ProjectPortfolio> images;

    private FragmentManager fragmentManager;

    public ProjectPortfolioGridViewAdapter(Context context, int layoutResourceId, List<ProjectPortfolio> images, FragmentManager fragmentManager) {
        super(context, layoutResourceId, images);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.images = images;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        ViewHolder holder = null;

        if (cell == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            cell = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) cell.findViewById(R.id.ivThumbnailPortfolio);

            cell.setTag(holder);
        } else {
            holder = (ViewHolder) cell.getTag();
        }

        holder.image.setImageBitmap(Image.base64ToBitmap(images.get(position).getImage()));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("image", images.get(position).getImage());

                FullScreenImageFragment fragment = new FullScreenImageFragment();
                fragment.setArguments(arguments);

                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();
                }
        });

        return cell;
    }
}
