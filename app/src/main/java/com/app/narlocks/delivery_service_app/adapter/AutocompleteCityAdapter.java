package com.app.narlocks.delivery_service_app.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity.R;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.service.CityService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AutocompleteCityAdapter extends ArrayAdapter implements Filterable {

    private List<City> cities;

    public AutocompleteCityAdapter(Context context, int resource) {
        super(context, resource);

        cities = new ArrayList();
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public City getItem(int position) {
        return cities.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.autocomplete_layout, parent, false);

        City city = cities.get(position);

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(city.getName());

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    try {
                        String term = constraint.toString();
                        cities = getCities(term);
                    } catch (Exception e) {
                        Log.i("HUS", "EXCEPTION " + e);
                    }
                    filterResults.values = cities;
                    filterResults.count = cities.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }

    private List<City> getCities(String term) {
        if(term != null && term.length() < 3) {
            return new ArrayList<City>();
        }

        try {
            CityService service = ServiceGenerator.createService(CityService.class);
            Call<List<City>> call = service.getByName(term);

            Response response = call.execute();

            if(response.code() == 200) {
                return (List<City>) response.body();
            } else {
                Log.i("OMGGGGGGGGG", ErrorConversor.getErrorMessage(response.errorBody()));
            }
        } catch (Exception ex) {
            Log.i("ERRO getCities();", ex.getMessage());
        }

        return new ArrayList<City>();
    }
}
