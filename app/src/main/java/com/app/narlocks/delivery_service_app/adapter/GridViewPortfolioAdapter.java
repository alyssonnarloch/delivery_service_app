package com.app.narlocks.delivery_service_app.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.narlocks.delivery_service_app.activity.DisplayImageActivity;
import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.ImageItem;

import java.util.List;

public class GridViewPortfolioAdapter extends ArrayAdapter {

    static class ViewHolder {
        ImageView image;
        ImageButton removeButton;
    }

    private Context context;
    private int layoutResourceId;
    private List data;

    public GridViewPortfolioAdapter(Context context, int layoutResourceId, List data) {
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.ivThumbnailPortfolio);
            holder.removeButton = (ImageButton) row.findViewById(R.id.ibRemoveImage);
            holder.removeButton.setTag(position);

           holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;

                    BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                    Bitmap imageBitmap = bitmapDrawable.getBitmap();

                    Intent i = new Intent(context, DisplayImageActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.putExtra("imageBase64", Image.bitmapToBase64(imageBitmap));

                    context.startActivity(i);
                }
            });

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("TAG MAROTA", (Integer) v.getTag() + "");
                    int position = (Integer) v.getTag();
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem imageItem = (ImageItem) data.get(position);
        holder.image.setImageBitmap(imageItem.getImage());

        return row;
    }
}
