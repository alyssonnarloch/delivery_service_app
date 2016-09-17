package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.util.Log;

import com.app.narlocks.delivery_service_app.activity.NewServiceProviderServicesActivity;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceTypeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class ServiceTypeTask extends AsyncTask<Void, Void, List<ServiceType>> {

    private NewServiceProviderServicesActivity activity;

    public ServiceTypeTask(NewServiceProviderServicesActivity servicesActivity) {
        this.activity = servicesActivity;
    }

    @Override
    protected List<ServiceType> doInBackground(Void... params) {
        try {
            ServiceTypeService service = ServiceGenerator.createService(ServiceTypeService.class);
            Call<List<ServiceType>> call = service.getAll();

            Response response = call.execute();

            if (response.code() == 200) {
                return (List<ServiceType>) response.body();
            } else {
                Log.i("ServiceType ADAPTER " + response.code(), ErrorConversor.getErrorMessage(response.errorBody()));
            }
        } catch (Exception ex) {
            Log.i("ERRO getServiceType();", ex.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<ServiceType> serviceTypes) {
        super.onPostExecute(serviceTypes);

        activity.setServiceTypes(serviceTypes);
        Log.i("TESTEEEEEEEE", "Irra");
    }
}
