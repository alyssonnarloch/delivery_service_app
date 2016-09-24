package com.app.narlocks.delivery_service_app.activity_task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.app.narlocks.delivery_service_app.activity.ClientUpdateFragment;
import com.app.narlocks.delivery_service_app.extras.ErrorConversor;
import com.app.narlocks.delivery_service_app.model.Client;
import com.app.narlocks.delivery_service_app.service.ClientService;
import com.app.narlocks.delivery_service_app.service.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class ClientUpdateLoadTask extends AsyncTask<Integer, Void, Boolean> {

    private Client client;
    private ClientUpdateFragment fragment;
    private String errorMessage;

    public ClientUpdateLoadTask(ClientUpdateFragment fragment) {
        this.fragment = fragment;
        this.errorMessage = "";
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean resultOk = true;

        try {
            ClientService clientService = ServiceGenerator.createService(ClientService.class);
            Call<Client> clientCall = clientService.getById(params[0]);
            Response clientResponse = clientCall.execute();

            if (clientResponse.code() == 200) {
                client = (Client) clientResponse.body();
            } else {
                resultOk = false;
                errorMessage += ErrorConversor.getErrorMessage(clientResponse.errorBody());
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
            this.fragment.loadContentViewComponents(client);
        } else {
            Toast.makeText(fragment.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}

