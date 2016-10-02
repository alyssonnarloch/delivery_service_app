package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.SPUpdatePortfolioFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SPUpdatePortfolioLoadTask extends AsyncTask<Integer, Void, Boolean> {

    private ServiceProvider serviceProvider;
    private List<ServiceType> serviceTypes;
    private SPUpdatePortfolioFragment fragment;
    private String errorMessage;

    public SPUpdatePortfolioLoadTask(SPUpdatePortfolioFragment fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ServiceProviderService serviceProviderService = ServiceGenerator.createService(ServiceProviderService.class);
            Call<ServiceProvider> serviceProviderCall = serviceProviderService.getById(params[0]);
            Response responseServiceProvider = serviceProviderCall.execute();

            if (responseServiceProvider.code() == 200) {
                serviceProvider = (ServiceProvider) responseServiceProvider.body();
            } else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(responseServiceProvider.errorBody());
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
            this.fragment.loadContentViewComponents(serviceProvider);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}