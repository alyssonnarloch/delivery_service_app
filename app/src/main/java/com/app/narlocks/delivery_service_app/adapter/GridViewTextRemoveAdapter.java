package com.app.narlocks.delivery_service_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.model.NameItem;

import java.util.List;

public class GridViewTextRemoveAdapter extends ArrayAdapter {

    static class ViewHolder {
        TextView tvName;
        ImageButton ibRemove;
    }

    private Context context;
    private int layoutResourceId;
    private List data;

    public GridViewTextRemoveAdapter(Context context, int layoutResourceId, List data) {
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.tvName = (TextView) row.findViewById(R.id.tvName);
            holder.ibRemove = (ImageButton) row.findViewById(R.id.ibRemove);
            holder.ibRemove.setTag(position);

            holder.ibRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        NameItem nameItem = (NameItem) data.get(position);
        holder.tvName.setText(nameItem.getName());

        return row;
    }
}
