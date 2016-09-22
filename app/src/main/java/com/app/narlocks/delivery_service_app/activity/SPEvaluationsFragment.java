package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.narlocks.delivery_service_app.activity_task.SPEvaluationsTask;
import com.app.narlocks.delivery_service_app.adapter.EvaluationListAdapter;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.User;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

public class SPEvaluationsFragment extends Fragment {

    private ListView lvServiceProviderEvaluations;

    private SessionManager session;
    private Resources res;

    public SPEvaluationsFragment() {

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

        getActivity().setTitle(R.string.title_service_provider_evaluations);

        View view = inflater.inflate(R.layout.fragment_sp_evaluations, container, false);

        loadViewComponents(view);
        new SPEvaluationsTask(this).execute(session.getUserId());

        return view;
    }

    private void loadViewComponents(View view) {
        lvServiceProviderEvaluations = (ListView) view.findViewById(R.id.lvServiceProviderEvaluations);
    }

    public void loadContentViewComponents(List<Project> serviceProviderProjects) {
        EvaluationListAdapter adapter = new EvaluationListAdapter(getActivity(), serviceProviderProjects, User.SERVICE_PROVIDER, getActivity().getSupportFragmentManager());
        lvServiceProviderEvaluations.setAdapter(adapter);
    }
}
