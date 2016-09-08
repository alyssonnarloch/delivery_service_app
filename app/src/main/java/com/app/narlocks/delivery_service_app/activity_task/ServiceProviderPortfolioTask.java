package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.ServiceProviderPortfolioFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ProjectPortfolio;
import com.app.narlocks.delivery_service_app.model.ServiceProviderPortfolio;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ServiceProviderPortfolioTask extends AsyncTask<Integer, Void, Boolean> {

    private List<ServiceProviderPortfolio> serviceProviderPortfolio;
    private ServiceProviderPortfolioFragment fragment;
    private String errorMessage;

    public ServiceProviderPortfolioTask(ServiceProviderPortfolioFragment fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ServiceProviderService serviceProviderService = ServiceGenerator.createService(ServiceProviderService.class);
            Call<List<ServiceProviderPortfolio>> serviceProviderCall = serviceProviderService.getPortfolio(params[0]);
            Response serviceProviderResponse = serviceProviderCall.execute();

            if (serviceProviderResponse.code() == 200) {
                serviceProviderPortfolio = (List<ServiceProviderPortfolio>) serviceProviderResponse.body();
            } else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(serviceProviderResponse.errorBody());
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
            this.fragment.loadContentViewComponents(serviceProviderPortfolio);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}