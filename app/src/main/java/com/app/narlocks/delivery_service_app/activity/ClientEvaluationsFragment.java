package com.app.narlocks.delivery_service_app.activity;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientEvaluationsFragment extends Fragment {

    private SessionManager session;
    private Resources res;
    private ListView lvClientEvaluations;

    public ClientEvaluationsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_evaluations, container, false);

        res = getResources();
        session = new SessionManager(getActivity());
        lvClientEvaluations = (ListView) view.findViewById(R.id.lvClientEvaluations);

        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> projectCall = projectService.clientEvaluations(session.getUserId());

        projectCall.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.code() == 200) {
                    List<Project> clientProjects = response.body();
                    ProjectListAdapter adapter = new ProjectListAdapter(getActivity(), clientProjects);
                    lvClientEvaluations.setAdapter(adapter);
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
