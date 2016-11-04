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
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.service.ClientService;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDetailsFragment extends Fragment {

    private SessionManager session;
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
    Resources res;

    public ClientDetailsFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_client_perfil);

        View view = inflater.inflate(R.layout.fragment_client_details, container, false);

        res = getResources();
        session = new SessionManager(getActivity());

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
        btUpdate = (Button) view.findViewById(R.id.btUpdate);

        setClientData(session.getUserId());

        tvEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ClientEvaluationsFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putInt("clientId", session.getUserId());

                Fragment fragment = new ClientUpdateFragment();
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        return view;
    }

    private void setClientData(int id) {
        ClientService clientService = ServiceGenerator.createService(ClientService.class);
        Call<Client> clientCall = clientService.getById(id);

        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> projectCall = projectService.clientEvaluations(id);

        clientCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if(response.code() == 200) {
                    Client client = response.body();

                    ivProfileImageDetail.setImageBitmap(Image.base64ToBitmap(client.getProfileImage()));
                    tvName.setText(client.getName());
                    tvEmail.setText(client.getEmail());
                    tvPhone.setText(client.getPhone());
                    tvZipCode.setText(client.getZipCode());
                    tvState.setText(client.getCity().getState().getName());
                    tvCity.setText(client.getCity().getName());
                    tvAddress.setText(client.getAddress() + ", " + client.getNumber());
                } else {
                    Toast.makeText(getActivity(), res.getString(R.string.service_client_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(getActivity(), res.getString(R.string.service_client_fail), Toast.LENGTH_LONG).show();
            }
        });

        projectCall.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.code() == 200) {
                    List<Project> clientProjects = response.body();

                    int sum = 0;
                    double averageEvaluation = 0.0;
                    int numProjects = clientProjects.size();
                    String evaluationLabel = "";

                    if(numProjects > 1 || numProjects == 0) {
                        evaluationLabel = res.getString(R.string.evaluations);
                    } else {
                        evaluationLabel = res.getString(R.string.evaluation);
                    }

                    tvEvaluation.setText("(" + numProjects + " " + evaluationLabel + ")");

                    for(Project project : clientProjects) {
                        sum += project.getClientQualification();
                    }

                    if(numProjects != 0) {
                        averageEvaluation = sum / (double) numProjects;
                    }

                    rbStars.setRating((float) averageEvaluation);
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
}
