package com.app.narlocks.delivery_service_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.narlocks.delivery_service_app.activity.FullScreenImageFragment;
import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;
import com.app.narlocks.delivery_service_app.service.ProjectPortfolioService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesToApproveGridViewAdapter extends ArrayAdapter {

    static class ViewHolder {
        ImageView ivImage;
        ImageButton ibApprove;
        ImageButton ibReject;
    }

    private Context context;
    private int layoutResourceId;
    private List<ProjectPortfolio> portfolio;

    private FragmentManager fragmentManager;

    public ImagesToApproveGridViewAdapter(Context context, int layoutResourceId, List<ProjectPortfolio> portfolio, FragmentManager fragmentManager) {
        super(context, layoutResourceId, portfolio);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.portfolio = portfolio;
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
            holder.ivImage = (ImageView) cell.findViewById(R.id.ivImage);
            holder.ibApprove = (ImageButton) cell.findViewById(R.id.ibApprove);
            holder.ibReject = (ImageButton) cell.findViewById(R.id.ibReject);

            if(portfolio.get(position).isApproved() != null) {
                holder.ibApprove.setVisibility(View.INVISIBLE);
                holder.ibReject.setVisibility(View.INVISIBLE);
            }

            holder.ibApprove.setTag(position);
            holder.ibReject.setTag(position);

            holder.ibApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (Integer) v.getTag();
                    approveImage(position, true);
                }
            });

            holder.ibReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (Integer) v.getTag();
                    approveImage(position, false);
                }
            });

            cell.setTag(holder);
        } else {
            holder = (ViewHolder) cell.getTag();
        }

        holder.ivImage.setImageBitmap(Image.base64ToBitmap(portfolio.get(position).getImage()));
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("image", portfolio.get(position).getImage());

                FullScreenImageFragment fragment = new FullScreenImageFragment();
                fragment.setArguments(arguments);

                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();
            }
        });


        return cell;
    }

    private void approveImage(final int position, boolean approve) {
        ProjectPortfolioService service = ServiceGenerator.createService(ProjectPortfolioService.class);
        Call<ResponseBody> call = service.approveRejectImage(portfolio.get(position).getId(), approve);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    portfolio.remove(position);
                    notifyDataSetChanged();
                } else {
                    Log.i("ERROOOOOOO " + response.code(), response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("ERROOOOOOO", t.getMessage());
            }
        });
    }
}
