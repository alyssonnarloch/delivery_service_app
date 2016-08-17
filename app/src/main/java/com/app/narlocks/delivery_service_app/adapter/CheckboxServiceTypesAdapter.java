package com.app.narlocks.delivery_service_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.model.ServiceType;

import java.util.ArrayList;
import java.util.List;

public class CheckboxServiceTypesAdapter extends ArrayAdapter<ServiceType> {

    private List<ServiceType> serviceTypes;

    public CheckboxServiceTypesAdapter(Context context, int textViewResourceId, List<ServiceType> serviceTypes) {
        super(context, textViewResourceId, serviceTypes);
        this.serviceTypes = new ArrayList();
        this.serviceTypes.addAll(serviceTypes);
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.checkbox_layout, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    ServiceType serviceType = (ServiceType) cb.getTag();

                    serviceType.setSelected(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ServiceType serviceType = serviceTypes.get(position);
        holder.code.setText(" (" + serviceType.getId() + ")");
        holder.name.setText(serviceType.getName());
        holder.name.setChecked(serviceType.isSelected());
        holder.name.setTag(serviceType);

        return convertView;
    }
}