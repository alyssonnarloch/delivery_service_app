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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.ClientServiceProviderFavorite;
import com.app.narlocks.delivery_service_app.model.Project;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.service.FavoriteService;
import com.app.narlocks.delivery_service_app.service.ProjectService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;
import com.app.narlocks.delivery_service_app.session.SessionManager;
import com.app.narlocks.delivery_service_app.view.ExpandHeightListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientServiceProviderDetailsFragment extends Fragment {

    private RatingBar rbStars;
    private LinearLayout llPortfolio;
    private LinearLayout llProjects;
    private LinearLayout llMakeContract;
    private LinearLayout llInterested;
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
    private ImageView ivInterested;

    private boolean isFavorite;
    private ClientServiceProviderFavorite favorite;
    private ServiceProvider serviceProvider;
    private int serviceProviderNumEvaluations;
    private double serviceProviderQualification;

    private SessionManager session;
    Resources res;

    public ClientServiceProviderDetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_service_provider_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_service_provider_details, container, false);

        final int serviceProviderId = getArguments().getInt("serviceProviderId");

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
        tvExperienceDescription = (TextView) view.findViewById(R.id.tvExperienceDescription);
        lvServiceTypes = (ListView) view.findViewById(R.id.lvServiceTypes);
        lvOccupationAreas = (ListView) view.findViewById(R.id.lvOccupationAreas);
        llPortfolio = (LinearLayout) view.findViewById(R.id.llPortfolio);
        llProjects = (LinearLayout) view.findViewById(R.id.llProjects);
        llMakeContract = (LinearLayout) view.findViewById(R.id.llMakeContract);
        llInterested = (LinearLayout) view.findViewById(R.id.llIntrested);
        ivInterested = (ImageView) view.findViewById(R.id.ivInterested);

        setServiceProviderData(serviceProviderId);

        tvEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceProvider != null) {
                    Bundle arguments = new Bundle();
                    arguments.putInt("serviceProviderId", serviceProvider.getId());

                    Fragment fragment = new ClientServiceProviderEvaluationsFragment();
                    fragment.setArguments(arguments);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });


        llPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceProvider != null) {
                    Bundle arguments = new Bundle();
                    arguments.putInt("serviceProviderId", serviceProvider.getId());

                    Fragment fragment = new ClientServiceProviderPortfolioFragment();
                    fragment.setArguments(arguments);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        llProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceProvider != null) {
                    Bundle arguments = new Bundle();
                    arguments.putInt("serviceProviderId", serviceProvider.getId());

                    Fragment fragment = new ClientServiceProviderProjectsFragment();
                    fragment.setArguments(arguments);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        llMakeContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceProvider != null) {
                    Bundle arguments = new Bundle();
                    arguments.putInt("serviceProviderId", serviceProvider.getId());
                    arguments.putDouble("serviceProviderQualification", serviceProviderQualification);
                    arguments.putString("serviceProviderName", serviceProvider.getName());
                    arguments.putInt("serviceProviderNumEvaluations", serviceProviderNumEvaluations);

                    Fragment fragment = new ClientMakeContractFragment();
                    fragment.setArguments(arguments);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_client_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        llInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteService favoriteService = ServiceGenerator.createService(FavoriteService.class);

                if (!isFavorite) {
                    Call<ClientServiceProviderFavorite> favoriteSaveCall = favoriteService.save(session.getUserId(), serviceProviderId);
                    favoriteSaveCall.enqueue(new Callback<ClientServiceProviderFavorite>() {
                        @Override
                        public void onResponse(Call<ClientServiceProviderFavorite> call, Response<ClientServiceProviderFavorite> response) {
                            if (response.code() == 200) {
                                favorite = response.body();
                                isFavorite = true;
                                ivInterested.setImageResource(R.mipmap.ic_star_black_24dp);
                            } else {
                                Toast.makeText(getActivity(), res.getString(R.string.service_favorite), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ClientServiceProviderFavorite> call, Throwable t) {
                            Toast.makeText(getActivity(), res.getString(R.string.service_favorite), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    if (favorite != null) {
                        Call<ResponseBody> favoriteDeleteCall = favoriteService.delete(favorite.getId());
                        favoriteDeleteCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    isFavorite = false;
                                    ivInterested.setImageResource(R.mipmap.ic_star_border_black_24dp);
                                } else {
                                    Toast.makeText(getActivity(), res.getString(R.string.service_favorite), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getActivity(), res.getString(R.string.service_favorite), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    private void setServiceProviderData(int serviceProviderId) {
        final ServiceProviderService serviceProviderService = ServiceGenerator.createService(ServiceProviderService.class);
        Call<ServiceProvider> serviceProviderCall = serviceProviderService.getById(serviceProviderId);

        ProjectService projectService = ServiceGenerator.createService(ProjectService.class);
        Call<List<Project>> projectCall = projectService.serviceProviderEvaluations(serviceProviderId);

        FavoriteService favoriteService = ServiceGenerator.createService(FavoriteService.class);
        Call<ClientServiceProviderFavorite> favoriteCall = favoriteService.getFavorite(session.getUserId(), serviceProviderId);

        serviceProviderCall.enqueue(new Callback<ServiceProvider>() {
            @Override
            public void onResponse(Call<ServiceProvider> call, Response<ServiceProvider> response) {
                if (response.code() == 200) {
                    List<String> serviceTypesName = new ArrayList();
                    List<String> occupationAreaName = new ArrayList();

                    serviceProvider = response.body();

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

                    for (City city : serviceProvider.getOccupationAreas()) {
                        occupationAreaName.add(" - " + city.getName());
                    }

                    ArrayAdapter serviceTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_simple_item_layout, R.id.tvItem, serviceTypesName);
                    ArrayAdapter occupationAreaAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_simple_item_layout, R.id.tvItem, occupationAreaName);

                    lvServiceTypes.setAdapter(serviceTypeAdapter);
                    ExpandHeightListView.getListViewSize(lvServiceTypes);

                    lvOccupationAreas.setAdapter(occupationAreaAdapter);
                    ExpandHeightListView.getListViewSize(lvOccupationAreas);
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

                    serviceProviderNumEvaluations = numProjects;
                    tvEvaluation.setText("(" + numProjects + " " + evaluationLabel + ")");

                    for (Project project : serviceProviderProjects) {
                        sum += project.getServiceProviderQualification();
                    }

                    if (numProjects > 0) {
                        averageEvaluation = sum / (double) numProjects;
                    }

                    serviceProviderQualification = averageEvaluation;
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

        favoriteCall.enqueue(new Callback<ClientServiceProviderFavorite>() {
            @Override
            public void onResponse(Call<ClientServiceProviderFavorite> call, Response<ClientServiceProviderFavorite> response) {
                if (response.code() == 200) {
                    favorite = response.body();

                    if (favorite != null) {
                        isFavorite = true;
                        ivInterested.setImageResource(R.mipmap.ic_star_black_24dp);
                    } else {
                        isFavorite = false;
                    }
                } else {
                    Toast.makeText(getActivity(), res.getString(R.string.service_favorite), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClientServiceProviderFavorite> call, Throwable t) {
                Toast.makeText(getActivity(), res.getString(R.string.service_favorite), Toast.LENGTH_LONG).show();
            }
        });
    }
}
