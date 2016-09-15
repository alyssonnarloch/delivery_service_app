package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.ServiceProviderSearchListFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ServiceProviderSearchListTask extends AsyncTask<Void, Void, Boolean> {

    private List<ServiceProvider> serviceProviders;
    private ServiceProviderSearchListFragment fragment;
    private String errorMessage;

    private String serviceProviderName;
    private List<Integer> serviceTypeIds;
    private int cityId;
    private boolean available;

    public ServiceProviderSearchListTask(ServiceProviderSearchListFragment fragment, String serviceProviderName, List<Integer> serviceTypeIds, int cityId, boolean available) {
        this.serviceProviders = new ArrayList();
        this.fragment = fragment;
        this.errorMessage = "";

        this.serviceProviderName = serviceProviderName;
        this.serviceTypeIds = serviceTypeIds;
        this.cityId = cityId;
        this.available = available;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean resultOk = true;

        try {
            ServiceProviderService serviceProviderService = ServiceGenerator.createService(ServiceProviderService.class);
            Call<List<ServiceProvider>> serviceProviderCall = serviceProviderService.search(serviceProviderName, serviceTypeIds, cityId, available);
            Response response = serviceProviderCall.execute();

            if(response.code() == 200) {
                serviceProviders = (List<ServiceProvider>) response.body();
            }else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(response.errorBody());
            }

        } catch (Exception ex) {
            resultOk = false;
            ex.getStackTrace();
            errorMessage += ex.getMessage();
        }

        return resultOk;
    }

    @Override
    protected void onPostExecute(Boolean ok) {
        if (ok) {
            this.fragment.loadContentViewComponents(serviceProviders);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
