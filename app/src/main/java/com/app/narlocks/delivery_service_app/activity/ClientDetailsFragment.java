package com.app.narlocks.delivery_service_app.activity;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private LinearLayout llStars;
    private ImageView ivProfileImageDetail;
    private TextView tvEvaluation;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvZipCode;
    private TextView tvState;
    private TextView tvCity;
    private TextView tvAddress;
    Resources res;

    public ClientDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_details, container, false);

        res = getResources();
        session = new SessionManager(getActivity());

        ivProfileImageDetail = (ImageView) view.findViewById(R.id.ivProfileImageDetail);
        llStars = (LinearLayout) view.findViewById(R.id.llStars);
        tvEvaluation = (TextView) view.findViewById(R.id.tvEvaluation);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvZipCode = (TextView) view.findViewById(R.id.tvZipCode);
        tvState = (TextView) view.findViewById(R.id.tvState);
        tvCity = (TextView) view.findViewById(R.id.tvCity);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);

        setClientData(session.getUserId());

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

                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

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
                        averageEvaluation = sum / numProjects;
                    }
                    
                    setStarsEvaluation(averageEvaluation);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

            }
        });
    }

    private void setStarsEvaluation(double qualification) {
        int intPart = (int) qualification;

        for(int i = 1; i <= intPart; i++) {
            ImageView ivStar = new ImageView(getActivity());

            if(i == intPart && qualification > 1 && qualification <= (i + 0.9)) {
                ivStar.setImageResource(R.mipmap.ic_star_half_black_24dp);
            } else {
                ivStar.setImageResource(R.mipmap.ic_star_black_24dp);
            }
            llStars.addView(ivStar);
        }

        if(qualification == 0) {
            ImageView ivStar = new ImageView(getActivity());
            ivStar.setImageResource(R.mipmap.ic_star_border_black_24dp);

            llStars.addView(ivStar);
        }
    }
}
