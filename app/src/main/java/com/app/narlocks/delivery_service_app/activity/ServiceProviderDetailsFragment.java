package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceProviderDetailsFragment extends Fragment {

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
    private TextView tvExperienceDescription;
    private ListView lvServiceTypes;
    private ListView lvOccupationAreas;
    Resources res;

    public ServiceProviderDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_provider_details, container, false);

        int serviceProviderId = getArguments().getInt("serviceProviderId");

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
        tvExperienceDescription = (TextView) view.findViewById(R.id.tvExperienceDescription);
        lvServiceTypes = (ListView) view.findViewById(R.id.lvServiceTypes);
        lvOccupationAreas = (ListView) view.findViewById(R.id.lvOccupationAreas);

        setServiceProviderData(serviceProviderId);

        return view;
    }

    private void setServiceProviderData(int id) {
        ServiceProviderService serviceProviderService = ServiceGenerator.createService(ServiceProviderService.class);
        Call<ServiceProvider> serviceProviderCall = serviceProviderService.getById(id);

        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> projectCall = projectService.serviceProviderEvaluations(id);

        serviceProviderCall.enqueue(new Callback<ServiceProvider>() {
            @Override
            public void onResponse(Call<ServiceProvider> call, Response<ServiceProvider> response) {
                if (response.code() == 200) {
                    List<String> serviceTypesName = new ArrayList();
                    List<String> occupationAreaName = new ArrayList();

                    ServiceProvider serviceProvider = response.body();

                    ivProfileImageDetail.setImageBitmap(Image.base64ToBitmap(serviceProvider.getProfileImage()));
                    tvName.setText(serviceProvider.getName());
                    tvEmail.setText(serviceProvider.getEmail());
                    tvPhone.setText(serviceProvider.getPhone());
                    tvZipCode.setText(serviceProvider.getZipCode());
                    tvState.setText(serviceProvider.getCity().getState().getName());
                    tvCity.setText(serviceProvider.getCity().getName());
                    tvAddress.setText(serviceProvider.getAddress() + ", " + serviceProvider.getNumber());
                    tvExperienceDescription.setText(serviceProvider.getExperienceDescription());

                    for (ServiceType serviceType : serviceProvider.getServiceTypes()) {
                        serviceTypesName.add(" - " + serviceType.getName());
                    }

                    for(City city : serviceProvider.getOccupationAreas()) {
                        occupationAreaName.add(" - " + city.getName());
                    }

                    ArrayAdapter serviceTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_layout, R.id.tvItem, serviceTypesName);
                    ArrayAdapter occupationAreaAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_layout, R.id.tvItem, occupationAreaName);

                    lvServiceTypes.setAdapter(serviceTypeAdapter);
                    lvOccupationAreas.setAdapter(occupationAreaAdapter);
                } else {
                    Toast.makeText(getActivity(), res.getString(R.string.service_service_provider_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceProvider> call, Throwable t) {
                Toast.makeText(getActivity(), res.getString(R.string.service_service_provider_fail), Toast.LENGTH_LONG).show();
            }
        });

        projectCall.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if (response.code() == 200) {
                    List<Project> serviceProviderProjects = response.body();

                    int sum = 0;
                    double averageEvaluation = 0.0;
                    int numProjects = serviceProviderProjects.size();
                    String evaluationLabel = "";

                    if (numProjects > 1 || numProjects == 0) {
                        evaluationLabel = res.getString(R.string.evaluations);
                    } else {
                        evaluationLabel = res.getString(R.string.evaluation);
                    }

                    tvEvaluation.setText("(" + numProjects + " " + evaluationLabel + ")");

                    for (Project project : serviceProviderProjects) {
                        sum += project.getServiceProviderQualification();
                    }

                    if (numProjects != 0) {
                        averageEvaluation = sum / (double) numProjects;
                    }

                    setStarsEvaluation(averageEvaluation);
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

    private void setStarsEvaluation(double qualification) {
        int intPart = (int) qualification;

        for (int i = 1; i <= intPart; i++) {
            ImageView ivStar = new ImageView(getActivity());

            if (i == intPart && qualification > i && qualification <= (i + 0.9)) {
                ivStar.setImageResource(R.mipmap.ic_star_half_black_24dp);
            } else {
                ivStar.setImageResource(R.mipmap.ic_star_black_24dp);
            }
            llStars.addView(ivStar);
        }

        if (qualification == 0) {
            ImageView ivStar = new ImageView(getActivity());
            ivStar.setImageResource(R.mipmap.ic_star_border_black_24dp);

            llStars.addView(ivStar);
        }
    }
}
