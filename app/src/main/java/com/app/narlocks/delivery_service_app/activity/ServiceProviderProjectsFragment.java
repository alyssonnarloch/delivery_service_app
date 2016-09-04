package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.narlocks.delivery_service_app.adapter.ServiceProviderProjectsListAdapter;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceProviderProjectsFragment extends Fragment {

    private ListView lvServiceProviderProjects;

    public ServiceProviderProjectsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_provider_projects, container, false);

        lvServiceProviderProjects = (ListView) view.findViewById(R.id.lvServiceProviderProjects);

        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> callProjectService = projectService.serviceProviderProjects(getArguments().getInt("serviceProviderId"), Project.FINISHED);

        callProjectService.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.code() == 200) {
                    List<Project> serviceProviderProjects = response.body();

                    ServiceProviderProjectsListAdapter adapter = new ServiceProviderProjectsListAdapter(getActivity(), serviceProviderProjects, getActivity().getSupportFragmentManager());
                    lvServiceProviderProjects.setAdapter(adapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

            }
        });

        return view;
    }

}