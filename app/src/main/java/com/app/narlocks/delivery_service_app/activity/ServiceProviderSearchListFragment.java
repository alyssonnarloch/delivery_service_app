package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.narlocks.delivery_service_app.activity_task.ServiceProviderTask;
import com.app.narlocks.delivery_service_app.adapter.ServiceProviderListAdapter;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;

import java.util.List;

public class ServiceProviderSearchListFragment extends Fragment {

    private ListView lvServiceProvider;

    private ServiceProviderListAdapter serviceProviderListAdapter;

    public ServiceProviderSearchListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_provider_search_list, container, false);

        loadViewComponents(view);
        new ServiceProviderTask(this,
                getArguments().getString("serviceProviderName"),
                getArguments().getIntegerArrayList("serviceTypeIds"),
                getArguments().getInt("cityId"),
                getArguments().getBoolean("available")).execute();

        return view;
    }

    private void loadViewComponents(View view){
        lvServiceProvider = (ListView) view.findViewById(R.id.lvServiceProvider);
    }

    public void loadContentViewComponents(List<ServiceProvider> serviceProviders) {
        serviceProviderListAdapter = new ServiceProviderListAdapter(getActivity(), serviceProviders, getActivity().getSupportFragmentManager());
        lvServiceProvider.setAdapter(serviceProviderListAdapter);
    }

}
