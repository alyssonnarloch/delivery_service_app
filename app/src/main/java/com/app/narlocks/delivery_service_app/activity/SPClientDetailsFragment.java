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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.activity_task.SPClientDetailsTask;
import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

public class SPClientDetailsFragment extends Fragment {

    private RatingBar rbStars;
    private ImageView ivProfileImageDetail;
    private TextView tvEvaluation;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvZipCode;
    private TextView tvState;
    private TextView tvCity;
    private TextView tvAddress;
    private Button btUpdate;

    private int clientId;

    private SessionManager session;
    Resources res;

    public SPClientDetailsFragment() {

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

        getActivity().setTitle(R.string.title_service_provider_client_details);

        View view = inflater.inflate(R.layout.fragment_sp_client_details, container, false);

        loadViewComponents(view);
        new SPClientDetailsTask(this).execute(getArguments().getInt("clientId"));

        return view;
    }

    private void loadViewComponents(View view) {
        ivProfileImageDetail = (ImageView) view.findViewById(R.id.ivProfileImageDetail);
        rbStars = (RatingBar) view.findViewById(R.id.rbStars);
        tvEvaluation = (TextView) view.findViewById(R.id.tvEvaluation);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvZipCode = (TextView) view.findViewById(R.id.tvZipCode);
        tvState = (TextView) view.findViewById(R.id.tvState);
        tvCity = (TextView) view.findViewById(R.id.tvCity);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
    }

    private void loadViewComponentListeners() {
        tvEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putInt("clientId", clientId);

                Fragment fragment = new ClientEvaluationsFragment();
                fragment.setArguments(arguments);
                goToFragment(fragment);
            }
        });
    }

    public void loadContentViewComponents(Client client, List<Project> projects) {
        ivProfileImageDetail.setImageBitmap(Image.base64ToBitmap(client.getProfileImage()));
        tvName.setText(client.getName());
        tvEmail.setText(client.getEmail());
        tvPhone.setText(client.getPhone());
        tvZipCode.setText(client.getZipCode());
        tvState.setText(client.getCity().getState().getName());
        tvCity.setText(client.getCity().getName());
        tvAddress.setText(client.getAddress() + ", " + client.getNumber());

        clientId = client.getId();

        setStarsEvaluation(projects);
        loadViewComponentListeners();
    }

    private void setStarsEvaluation(List<Project> projects) {
        int sum = 0;
        double averageEvaluation = 0.0;
        int numProjects = projects.size();
        String evaluationLabel = "";

        if (numProjects > 1 || numProjects == 0) {
            evaluationLabel = res.getString(R.string.evaluations);
        } else {
            evaluationLabel = res.getString(R.string.evaluation);
        }

        tvEvaluation.setText("(" + numProjects + " " + evaluationLabel + ")");

        for (Project project : projects) {
            sum += project.getClientQualification();
        }

        if (numProjects != 0) {
            averageEvaluation = sum / (double) numProjects;
        }

        rbStars.setRating((float) averageEvaluation);
    }

    private void goToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_sp_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
