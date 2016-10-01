package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.adapter.ClientProjectsListAdapter;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientProjectsFragment extends Fragment implements View.OnClickListener {

    private SessionManager session;
    private Resources res;
    private ListView lvClientProjects;
    private ImageView ivAwaiting;
    private ImageView ivRefused;
    private ImageView ivExecution;
    private ImageView ivFinished;

    public ClientProjectsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_my_projects);

        View view = inflater.inflate(R.layout.fragment_client_projects, container, false);

        res = getResources();
        session = new SessionManager(getActivity());
        lvClientProjects = (ListView) view.findViewById(R.id.lvClientProjects);
        ivAwaiting = (ImageView) view.findViewById(R.id.ivAwaiting);
        ivRefused = (ImageView) view.findViewById(R.id.ivRefused);
        ivExecution = (ImageView) view.findViewById(R.id.ivExecution);
        ivFinished = (ImageView) view.findViewById(R.id.ivFinished);

        ivAwaiting.setOnClickListener(this);
        ivRefused.setOnClickListener(this);
        ivExecution.setOnClickListener(this);
        ivFinished.setOnClickListener(this);

        List<Integer> status = new ArrayList();
        listProjectsByStatus(status);

        return view;
    }

    private void listProjectsByStatus(List<Integer> status){
        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> projectCall = projectService.clientProjects(session.getUserId(), status);

        projectCall.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.code() == 200) {
                    List<Project> clientProjects = response.body();
                    ClientProjectsListAdapter adapter = new ClientProjectsListAdapter(getActivity(), clientProjects, getActivity().getSupportFragmentManager());
                    lvClientProjects.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), res.getString(R.string.service_project_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Toast.makeText(getActivity(), res.getString(R.string.service_project_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        List<Integer> status = new ArrayList();

        switch (v.getId()){
            case R.id.ivAwaiting:
                status.add(Project.AWATING);
                break;
            case R.id.ivRefused:
                status.add(Project.REFUSED);
                break;
            case R.id.ivExecution:
                status.add(Project.EXECUTION);
                break;
            case R.id.ivFinished:
                status.add(Project.FINISHED);
                break;
        }
        listProjectsByStatus(status);
    }
}
