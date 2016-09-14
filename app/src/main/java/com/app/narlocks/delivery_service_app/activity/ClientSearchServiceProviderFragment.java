package com.app.narlocks.delivery_service_app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.app.narlocks.delivery_service_app.activity_task.ServiceTypeTask;
import com.app.narlocks.delivery_service_app.adapter.AutocompleteCityAdapter;
import com.app.narlocks.delivery_service_app.adapter.ServiceTypesCheckboxAdapter;
import com.app.narlocks.delivery_service_app.model.City;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.session.SessionManager;
import com.app.narlocks.delivery_service_app.view.ExpandHeightListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientSearchServiceProviderFragment extends Fragment {

    private EditText etServiceProviderName;
    private ListView lvServiceTypes;
    private CheckBox cbUseMyAddress;
    private AutoCompleteTextView acCity;
    private CheckBox cbAvailable;
    //private RatingBar rbQualification;
    private Button btSearch;

    AutocompleteCityAdapter cityAdapter;
    ServiceTypesCheckboxAdapter serviceTypeAdapter;

    private int selectedCityId = 0;
    private String selectedCityName;

    private SessionManager session;
    Resources res;

    public ClientSearchServiceProviderFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_search_service, container, false);

        res = getResources();
        session = new SessionManager(getActivity());

        loadViewComponents(view);
        new ServiceTypeTask(this).execute();

        return view;
    }

    private void loadViewComponents(View view) {
        etServiceProviderName = (EditText) view.findViewById(R.id.etServiceProviderName);
        lvServiceTypes = (ListView) view.findViewById(R.id.lvServiceTypes);
        cbUseMyAddress = (CheckBox) view.findViewById(R.id.cbUseMyAddress);
        acCity = (AutoCompleteTextView) view.findViewById(R.id.acCity);
        cbAvailable = (CheckBox) view.findViewById(R.id.cbAvailable);
        //rbQualification = (RatingBar) view.findViewById(R.id.rbQualification);
        btSearch = (Button) view.findViewById(R.id.btSearch);

        cbUseMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbUseMyAddress.isChecked()) {
                    acCity.setEnabled(false);
                    acCity.setText(session.getUserCityName());
                    selectedCityId = session.getUserCityId();
                    selectedCityName = session.getUserCityName();
                    acCity.setError(null);
                } else {
                    acCity.setEnabled(true);
                    acCity.setText("");
                    selectedCityId = 0;
                    selectedCityName = "";
                }
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("serviceProviderName", etServiceProviderName.getText().toString());
                arguments.putIntegerArrayList("serviceTypeIds", getServiceTypeCheckeds());
                arguments.putInt("cityId", selectedCityId);
                arguments.putBoolean("available", cbAvailable.isChecked());

                Fragment fragment = new ServiceProviderSearchListFragment();
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_client, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        loadCitiesView();
    }

    public void loadServiceTypesView(List<ServiceType> serviceTypes) {
        serviceTypeAdapter = new ServiceTypesCheckboxAdapter(getActivity(), R.layout.checkbox_layout, serviceTypes);
        lvServiceTypes.setAdapter(serviceTypeAdapter);
        ExpandHeightListView.getListViewSize(lvServiceTypes);
    }

    public void loadCitiesView() {
        cityAdapter = new AutocompleteCityAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        acCity.setAdapter(cityAdapter);

        acCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = cityAdapter.getItem(position);

                selectedCityId = selectedCity.getId();
                selectedCityName = selectedCity.getName();
                acCity.setText(selectedCity.getName());
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
    }

    public ArrayList<Integer> getServiceTypeCheckeds(){
        ArrayList<Integer> serviceTypeIdCheckeds = new ArrayList();

        for (Map.Entry<Integer, Boolean> entryServiceType : serviceTypeAdapter.getServiceTypesCheck().entrySet()) {
            if (entryServiceType.getValue() == true) {
                serviceTypeIdCheckeds.add(entryServiceType.getKey());
            }
        }

        Log.i("TESTEEEEE", serviceTypeIdCheckeds.size() + "");

        return serviceTypeIdCheckeds;
    }

}
