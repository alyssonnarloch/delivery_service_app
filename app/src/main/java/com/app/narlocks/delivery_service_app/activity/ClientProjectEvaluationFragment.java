package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity_task.ClientEvaluationAwaitingTask;
import com.app.narlocks.delivery_service_app.model.User;
import com.app.narlocks.delivery_service_app.session.SessionManager;

public class ClientProjectEvaluationFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvServiceProviderName;
    private EditText etDescription;
    private RatingBar rbQualification;
    private Button btFinish;

    private int projectId;
    private int newProjectStatus;

    private SessionManager session;
    private Resources res;

    public ClientProjectEvaluationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_finish_projetct);

        this.res = getResources();
        this.session = new SessionManager(getActivity());
        this.projectId = getArguments().getInt("projectId");
        this.newProjectStatus = getArguments().getInt("newProjectStatus");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_evaluation_awaiting, container, false);

        loadViewComponents(view);
        loadViewListeners(view);
        loadContentViewComponents();

        return view;
    }

    private void loadViewComponents(View view) {
        //Resolve o problema do teclado ficar sobre o input
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvServiceProviderName = (TextView) view.findViewById(R.id.tvServiceProviderName);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        rbQualification = (RatingBar) view.findViewById(R.id.rbQualification);
        btFinish = (Button) view.findViewById(R.id.btFinish);
    }

    private void loadViewListeners(View view) {
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishProject();
            }
        });
    }

    private void loadContentViewComponents() {
        tvTitle.setText(getArguments().getString("title"));
        tvStatus.setText(getArguments().getString("status"));
        tvServiceProviderName.setText(getArguments().getString("serviceProviderName"));
    }

    private void finishProject() {
        int qualification = 0;
        String description = "";

        description = etDescription.getText().toString();
        qualification = (int) rbQualification.getRating();

        new ClientEvaluationAwaitingTask(this, projectId, qualification, description, User.SERVICE_PROVIDER, newProjectStatus).execute();
    }

    public void loadClientProjects() {
        Toast.makeText(getActivity(), res.getString(R.string.project_finished_ok), Toast.LENGTH_LONG).show();

        Fragment fragment = new ClientProjectsFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
