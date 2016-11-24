package com.app.narlocks.delivery_service_app.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;

import com.app.narlocks.delivery_service_app.activity_task.SPUpdateAreasLoadTask;
import com.app.narlocks.delivery_service_app.activity_task.SPUpdateAreasSaveTask;
import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.adapter.GridViewTextRemoveAdapter;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.NameItem;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SPUpdateAreasFragment extends Fragment {

    private AutoCompleteTextView acCity;
    private CheckBox cbAvailable;
    private GridView gvSelectedCities;
    private Button btUpdate;

    private AutocompleteCityAdapter acCityAdapter;
    private GridViewTextRemoveAdapter gvTextAdapter;
    private List<NameItem> cities;

    private ServiceProvider serviceProvider;
    private int selectedCityId = 0;
    private String selectedCityName;

    private SessionManager session;
    private Resources res;

    public SPUpdateAreasFragment() {

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

        getActivity().setTitle(R.string.title_service_provider_update);

        View view = inflater.inflate(R.layout.fragment_sp_update_areas, container, false);

        this.cities = new ArrayList();

        loadViewComponents(view);
        loadViewComponentsListeners();
        new SPUpdateAreasLoadTask(this).execute(session.getUserId());

        return view;
    }

    private void loadViewComponents(View view) {
        cbAvailable = (CheckBox) view.findViewById(R.id.cbAvailable);
        gvSelectedCities = (GridView) view.findViewById(R.id.gvSelectedCities);
        acCity = (AutoCompleteTextView) view.findViewById(R.id.acCity);
        acCity.setInputType(InputType.TYPE_CLASS_TEXT);
        acCityAdapter = new AutocompleteCityAdapter(getActivity(), R.layout.autocomplete_layout);
        acCity.setAdapter(acCityAdapter);
        btUpdate = (Button) view.findViewById(R.id.btUpdate);
    }

    private void loadViewComponentsListeners() {
        acCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Fecha o teclado
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(acCity.getWindowToken(), 0);

                City selectedCity = acCityAdapter.getItem(position);

                selectedCityId = selectedCity.getId();
                selectedCityName = selectedCity.getName();
                acCity.setText(selectedCity.getName());

                cities.add(new NameItem(selectedCityId, selectedCityName));
                gvTextAdapter = new GridViewTextRemoveAdapter(getActivity(), R.layout.text_remove_layout, cities);
                gvSelectedCities.setAdapter(gvTextAdapter);

                acCity.setText(null);
            }
        });

        acCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (selectedCityName != null && !selectedCityName.equals(s.toString())) {
                    selectedCityId = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    public void loadContentViewComponents(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;

        for (City city : serviceProvider.getOccupationAreas()) {
            cities.add(new NameItem(city.getId(), city.getName()));
        }

        cbAvailable.setChecked(serviceProvider.isAvailable());

        gvTextAdapter = new GridViewTextRemoveAdapter(getActivity(), R.layout.text_remove_layout, cities);
        gvSelectedCities.setAdapter(gvTextAdapter);
    }

    public void backDashboard() {
        Fragment fragment = new SPDashboardFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_sp_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private boolean validate() {
        boolean isValid = true;

        if(cities.size() == 0) {
            isValid = false;
            acCity.setError(res.getString(R.string.service_provider_areas_required));
        }

        return isValid;
    }

    private void update() {
        serviceProvider.setAvailable(cbAvailable.isChecked());

        for(NameItem item : cities) {
            serviceProvider.addOccupationAreaId(item.getId());
        }

        if(validate()) {
            new SPUpdateAreasSaveTask(this, serviceProvider).execute();
        }
    }

}
