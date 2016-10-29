package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.narlocks.delivery_service_app.activity_task.SPProjectsTask;
import com.app.narlocks.delivery_service_app.adapter.ServiceProviderProjectsAdapter;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SPProjectsFragment extends Fragment implements View.OnClickListener {

    private ListView lvServiceProviderProjects;
    private LinearLayout ivAwaiting;
    private LinearLayout ivRefused;
    private LinearLayout ivExecution;
    private LinearLayout ivFinished;
    private LinearLayout ivAll;

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

        List<Integer> status = new ArrayList();
        listProjectsByStatus(status);

        return view;
    }

    private void loadViewComponents(View view) {
        lvServiceProviderProjects = (ListView) view.findViewById(R.id.lvServiceProviderProjects);
        ivAll = (LinearLayout) view.findViewById(R.id.ivAll);
        ivAwaiting = (LinearLayout) view.findViewById(R.id.ivAwaiting);
        ivRefused = (LinearLayout) view.findViewById(R.id.ivRefused);
        ivExecution = (LinearLayout) view.findViewById(R.id.ivExecution);
        ivFinished = (LinearLayout) view.findViewById(R.id.ivFinished);
    }

    private void loadViewComponentListeners() {
        ivAll.setOnClickListener(this);
        ivAwaiting.setOnClickListener(this);
        ivRefused.setOnClickListener(this);
        ivExecution.setOnClickListener(this);
        ivFinished.setOnClickListener(this);
    }

    private void listProjectsByStatus(List<Integer> status) {
        new SPProjectsTask(this, status).execute(session.getUserId());
    }

    public void loadContentViewComponents(List<Project> projects) {
        ServiceProviderProjectsAdapter adapter = new ServiceProviderProjectsAdapter(getActivity(), projects, getActivity().getSupportFragmentManager());
        lvServiceProviderProjects.setAdapter(adapter);
        lvServiceProviderProjects.setEmptyView(getActivity().findViewById(R.id.llEmptyInfo));
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
