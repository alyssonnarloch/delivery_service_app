package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.adapter.EvaluationListAdapter;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.User;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientServiceProviderEvaluationsFragment extends Fragment {

    private SessionManager session;
    private Resources res;
    private ListView lvServiceProviderEvaluations;

    public ClientServiceProviderEvaluationsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_service_provider_evaluation);

        View view = inflater.inflate(R.layout.fragment_client_service_provider_evaluations, container, false);

        res = getResources();
        session = new SessionManager(getActivity());
        lvServiceProviderEvaluations = (ListView) view.findViewById(R.id.lvServiceProviderEvaluations);

        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> projectCall = projectService.serviceProviderEvaluations(getArguments().getInt("serviceProviderId"));

        projectCall.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.code() == 200) {
                    List<Project> serviceProviderProjects = response.body();
                    EvaluationListAdapter adapter = new EvaluationListAdapter(getActivity(), serviceProviderProjects, User.SERVICE_PROVIDER, getActivity().getSupportFragmentManager());
                    lvServiceProviderEvaluations.setAdapter(adapter);
                    lvServiceProviderEvaluations.setEmptyView(getActivity().findViewById(R.id.llEmptyInfo));
                } else {
                    Toast.makeText(getActivity(), res.getString(R.string.service_project_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Toast.makeText(getActivity(), res.getString(R.string.service_project_fail), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
