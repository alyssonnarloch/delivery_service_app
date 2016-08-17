package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.adapter.CheckboxServiceTypesAdapter;
import com.app.narlocks.delivery_service_app.async_task.ServiceTypeTask;
import com.app.narlocks.delivery_service_app.model.ServiceType;

import java.util.List;

public class NewServiceProviderServicesActivity extends AppCompatActivity {

    private List<ServiceType> serviceTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_provider_services);

        new ServiceTypeTask(NewServiceProviderServicesActivity.this).execute();
    }

    public void setServiceTypes(List<ServiceType> listServiceTypes) {
        this.serviceTypes = listServiceTypes;

        displayServiceTypesView();
    }


    public void onClickNext(View view) {
        for(ServiceType s : this.serviceTypes) {
            Log.i("Service Type", s.getName() + " - " + s.isSelected());
        }
    }

    public void onClickCancel(View view) {

    }

    private void checkButtonClick() {
        /*Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                List<ServiceType> serviceTypesList = dataAdapter.getServiceTypes();
                for (int i = 0; i < serviceTypesList.size(); i++) {
                    ServiceType country = serviceTypesList.get(i);
                    if (country.isSelected()) {
                        responseText.append("\n" + country.getName());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });*/
    }

    private void displayServiceTypesView() {
        CheckboxServiceTypesAdapter dataAdapter = new CheckboxServiceTypesAdapter(this, R.layout.checkbox_layout, serviceTypes);

        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
