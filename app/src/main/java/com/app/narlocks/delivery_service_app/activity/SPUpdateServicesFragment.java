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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity_task.SPUpdateServicesLoadTask;
import com.app.narlocks.delivery_service_app.activity_task.SPUpdateServicesSaveTask;
import com.app.narlocks.delivery_service_app.adapter.ServiceTypesCheckboxAdapter;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.session.SessionManager;
import com.app.narlocks.delivery_service_app.view.ExpandHeightListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SPUpdateServicesFragment extends Fragment {

    private ListView lvServiceTypes;
    private EditText etExperienceDescription;
    private Button btUpdate;

    private ServiceProvider serviceProvider;
    private ServiceTypesCheckboxAdapter serviceTypesAdapter;

    private SessionManager session;
    private Resources res;

    public SPUpdateServicesFragment() {

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

        View view = inflater.inflate(R.layout.fragment_sp_update_services, container, false);

        loadViewComponents(view);
        loadViewComponentsListeners();
        new SPUpdateServicesLoadTask(this).execute(session.getUserId());

        return view;
    }

    private void loadViewComponents(View view) {
        lvServiceTypes = (ListView) view.findViewById(R.id.lvServiceTypes);
        etExperienceDescription = (EditText) view.findViewById(R.id.etExperienceDescription);
        btUpdate = (Button) view.findViewById(R.id.btUpdate);
    }

    private void loadViewComponentsListeners() {
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    public void loadContentViewComponents(ServiceProvider serviceProvider, List<ServiceType> serviceTypes) {
        this.serviceProvider = serviceProvider;

        etExperienceDescription.setText(serviceProvider.getExperienceDescription());

        serviceTypesAdapter = new ServiceTypesCheckboxAdapter(getActivity(), R.layout.checkbox_layout, getCheckedServiceTypes(serviceTypes, serviceProvider.getServiceTypes()));
        lvServiceTypes.setAdapter(serviceTypesAdapter);
        ExpandHeightListView.getListViewSize(lvServiceTypes);
    }

    private List<ServiceType> getCheckedServiceTypes(List<ServiceType> serviceTypes, Set<ServiceType> serviceProviderServiceTypes) {
        int i = 0;

        for (ServiceType serviceType : serviceTypes) {
            for (ServiceType serviceTypeSP : serviceProviderServiceTypes) {
                if (serviceTypeSP.getId() == serviceType.getId()) {
                    serviceTypes.get(i).setSelected(true);
                    serviceProvider.addServiceTypeId(serviceTypeSP.getId());
                }
            }
            i++;
        }
        return serviceTypes;
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

        int countSelectedServices = 0;
        for (Map.Entry<Integer, Boolean> entryServiceType : serviceTypesAdapter.getServiceTypesCheck().entrySet()) {
            if (entryServiceType.getValue() == true) {
                countSelectedServices++;
            }
        }

        if(countSelectedServices == 0) {
            isValid = false;
        }

        if(serviceProvider.getExperienceDescription() == null || serviceProvider.getExperienceDescription().isEmpty()) {
            isValid = false;
            etExperienceDescription.setError(res.getString(R.string.service_provider_experience_description_required));
        }

        return isValid;
    }

    private void update() {
        serviceProvider.setServiceTypeIds(new ArrayList());
        for (Map.Entry<Integer, Boolean> entryServiceType : serviceTypesAdapter.getServiceTypesCheck().entrySet()) {
            if (entryServiceType.getValue() == true) {
                serviceProvider.addServiceTypeId(entryServiceType.getKey());
            }
        }

        serviceProvider.setExperienceDescription(etExperienceDescription.getText().toString());

        if (validate()) {
            new SPUpdateServicesSaveTask(this, serviceProvider).execute();
        } else {
            Toast.makeText(getActivity(), R.string.service_provider_services_required, Toast.LENGTH_LONG).show();
        }
    }
}
