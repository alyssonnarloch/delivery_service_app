package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.ClientSearchServiceProviderFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.ServiceType;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;
import com.app.narlocks.delivery_service_app.service.ServiceTypeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ServiceTypeTask extends AsyncTask<Void, Void, Boolean> {

    private List<ServiceType> serviceTypes;
    private ClientSearchServiceProviderFragment fragment;
    private String errorMessage;

    public ServiceTypeTask(ClientSearchServiceProviderFragment fragment) {
        this.serviceTypes = new ArrayList();
        this.fragment = fragment;
        this.errorMessage = "";
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean resultOk = true;

        try {
            ServiceTypeService serviceTypeService = ServiceGenerator.createService(ServiceTypeService.class);
            Call<List<ServiceType>> serviceTypeCall = serviceTypeService.getAll();
            Response response = serviceTypeCall.execute();

            if (response.code() == 200) {
                serviceTypes = (List<ServiceType>) response.body();
            } else {
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
            this.fragment.loadServiceTypesView(serviceTypes);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
