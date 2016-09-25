package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.narlocks.delivery_service_app.activity_task.SPProjectsTask;
import com.app.narlocks.delivery_service_app.adapter.ServiceProviderProjectsAdapter;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

public class SPProjectsFragment extends Fragment implements View.OnClickListener {

    private ListView lvServiceProviderProjects;
    private ImageView ivAwaiting;
    private ImageView ivRefused;
    private ImageView ivExecution;
    private ImageView ivFinished;

    private SessionManager session;
    private Resources res;

    public SPProjectsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.res = getResources();
        this.session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_service_provider_projects_list);

        View view = inflater.inflate(R.layout.fragment_sp_projects, container, false);
        loadViewComponents(view);
        loadViewComponentListeners();
        listProjectsByStatus(0);

        return view;
    }

    private void loadViewComponents(View view) {
        lvServiceProviderProjects = (ListView) view.findViewById(R.id.lvServiceProviderProjects);
        ivAwaiting = (ImageView) view.findViewById(R.id.ivAwaiting);
        ivRefused = (ImageView) view.findViewById(R.id.ivRefused);
        ivExecution = (ImageView) view.findViewById(R.id.ivExecution);
        ivFinished = (ImageView) view.findViewById(R.id.ivFinished);
    }

    private void loadViewComponentListeners() {
        ivAwaiting.setOnClickListener(this);
        ivRefused.setOnClickListener(this);
        ivExecution.setOnClickListener(this);
        ivFinished.setOnClickListener(this);
    }

    private void listProjectsByStatus(int status) {
        new SPProjectsTask(this).execute(session.getUserId(), status);
    }

    public void loadContentViewComponents(List<Project> projects) {
        ServiceProviderProjectsAdapter adapter = new ServiceProviderProjectsAdapter(getActivity(), projects, getActivity().getSupportFragmentManager());
        lvServiceProviderProjects.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivAwaiting:
                listProjectsByStatus(Project.AWATING);
                break;
            case R.id.ivRefused:
                listProjectsByStatus(Project.REFUSED);
                break;
            case R.id.ivExecution:
                listProjectsByStatus(Project.EXECUTION);
                break;
            case R.id.ivFinished:
                listProjectsByStatus(Project.FINISHED);
                break;
        }
    }

}
