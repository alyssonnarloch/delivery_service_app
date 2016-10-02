package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.SPUpdateServicesFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ServiceProvider;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceProviderService;
import com.app.narlocks.delivery_service_app.service.ServiceTypeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SPUpdateServicesLoadTask extends AsyncTask<Integer, Void, Boolean> {

    private ServiceProvider serviceProvider;
    private List<ServiceType> serviceTypes;
    private SPUpdateServicesFragment fragment;
    private String errorMessage;

    public SPUpdateServicesLoadTask(SPUpdateServicesFragment fragment) {
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

            ServiceTypeService serviceTypeService = ServiceGenerator.createService(ServiceTypeService.class);
            Call<List<ServiceType>> serviceTypeCall = serviceTypeService.getAll();
            Response responseServiceType = serviceTypeCall.execute();

            if (responseServiceProvider.code() == 200) {
                serviceProvider = (ServiceProvider) responseServiceProvider.body();
            } else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(responseServiceProvider.errorBody());
            }

            if(responseServiceType.code() == 200) {
                serviceTypes = (List<ServiceType>) responseServiceType.body();
            } else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(responseServiceType.errorBody());
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
            this.fragment.loadContentViewComponents(serviceProvider, serviceTypes);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
