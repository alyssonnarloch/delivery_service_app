package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.adapter.ServiceProviderListAdapter;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.service.FavoriteService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientFavoritesFragment extends Fragment {

    private SessionManager session;
    private Resources res;
    private ListView lvFavorites;

    public ClientFavoritesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_client_favorites);

        final View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        res = getResources();
        session = new SessionManager(getActivity());
        lvFavorites = (ListView) view.findViewById(R.id.lvFavorites);

        FavoriteService favoriteService = ServiceGenerator.createService(FavoriteService.class);
        Call<List<ServiceProvider>> favoriteCall = favoriteService.getServicesProvider(session.getUserId());

        favoriteCall.enqueue(new Callback<List<ServiceProvider>>() {
            @Override
            public void onResponse(Call<List<ServiceProvider>> call, Response<List<ServiceProvider>> response) {
                if(response.code() == 200) {
                    List<ServiceProvider> serviceProviders = response.body();

                    ServiceProviderListAdapter adapter = new ServiceProviderListAdapter(getActivity(), serviceProviders, getActivity().getSupportFragmentManager());
                    lvFavorites.setAdapter(adapter);
                    lvFavorites.setEmptyView(view.findViewById(R.id.llEmptyInfo));
                } else {
                    Toast.makeText(getActivity(), res.getString(R.string.service_service_provider_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ServiceProvider>> call, Throwable t) {
                Toast.makeText(getActivity(), res.getString(R.string.service_service_provider_fail), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
