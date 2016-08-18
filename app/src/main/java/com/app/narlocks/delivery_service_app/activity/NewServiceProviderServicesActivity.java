package com.app.narlocks.delivery_service_app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.adapter.CheckboxServiceTypesAdapter;
import com.app.narlocks.delivery_service_app.async_task.ServiceTypeTask;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.model.ServiceType;

import java.util.HashSet;
import java.util.List;

public class NewServiceProviderServicesActivity extends AppCompatActivity {

    private ServiceProvider serviceProvider;
    private List<ServiceType> serviceTypes;
    private Resources res;
    private EditText etExperienceDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_provider_services);

        this.res = getResources();
        this.serviceProvider = (ServiceProvider) getIntent().getSerializableExtra("serviceProviderObj");
        new ServiceTypeTask(NewServiceProviderServicesActivity.this).execute();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void setServiceTypes(List<ServiceType> listServiceTypes) {
        this.serviceTypes = listServiceTypes;

        displayServiceTypesView();
    }

    public void onClickNext(View view) {
        getServiceProviderByView(view);

        if(validate()) {

        }
    }

    public void onClickCancel(View view) {

    }

    private void getDataViewContent(View view) {
        etExperienceDescription = (EditText) findViewById(R.id.etExperienceDescription);
    }

    private void getServiceProviderByView(View view) {
        getDataViewContent(view);

        this.serviceProvider.setExperienceDescription(etExperienceDescription.getText().toString());
        this.serviceProvider.setServiceTypes(new HashSet<ServiceType>());
        for(ServiceType serviceType : this.serviceTypes) {
            if(serviceType.isSelected()) {
                this.serviceProvider.addServiceType(serviceType);
            }
        }
    }

    private boolean validate() {
        boolean valid = true;

        CheckBox ck = (CheckBox) findViewById(R.id.check);
        if(this.serviceProvider.getServiceTypes().size() == 0) {
            valid = false;

            ck.setError(res.getString(R.string.validation_required));
        } else {
            ck.setError(null);
        }

        if(this.serviceProvider.getExperienceDescription() == null || this.serviceProvider.getExperienceDescription().equals("") || this.serviceProvider.getExperienceDescription().equals(" ")){
            valid = false;
            etExperienceDescription.setError(res.getString(R.string.validation_required));
        }

        return valid;
    }

    private void displayServiceTypesView() {
        CheckboxServiceTypesAdapter dataAdapter = new CheckboxServiceTypesAdapter(this, R.layout.checkbox_layout, serviceTypes);

        ListView lvServiceType = (ListView) findViewById(R.id.lvServiceTypes);
        lvServiceType.setAdapter(dataAdapter);

        lvServiceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ServiceType serviceType = (ServiceType) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + serviceType.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
