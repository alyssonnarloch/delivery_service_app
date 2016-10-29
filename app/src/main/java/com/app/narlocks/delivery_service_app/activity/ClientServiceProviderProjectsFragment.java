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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientServiceProviderProjectsFragment extends Fragment {

    private ListView lvServiceProviderProjects;

    public ClientServiceProviderProjectsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_service_provider_projects);

        View view = inflater.inflate(R.layout.fragment_client_service_provider_projects, container, false);

        lvServiceProviderProjects = (ListView) view.findViewById(R.id.lvServiceProviderProjects);

        List<Integer> status = new ArrayList();
        status.add(Project.REFUSED);
        status.add(Project.EXECUTION);
        status.add(Project.FINISHED);
        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> callProjectService = projectService.serviceProviderProjects(getArguments().getInt("serviceProviderId"), status);

        callProjectService.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.code() == 200) {
                    List<Project> serviceProviderProjects = response.body();

                    ServiceProviderProjectsListAdapter adapter = new ServiceProviderProjectsListAdapter(getActivity(), serviceProviderProjects, getActivity().getSupportFragmentManager());
                    lvServiceProviderProjects.setAdapter(adapter);
                    lvServiceProviderProjects.setEmptyView(getActivity().findViewById(R.id.llEmptyInfo));
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
