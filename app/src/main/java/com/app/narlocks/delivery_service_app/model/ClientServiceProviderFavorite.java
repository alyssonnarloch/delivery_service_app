package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;

public class ClientServiceProviderFavorite implements Serializable {

    private int id;
    private Client client;
    private ServiceProvider serviceProvider;

    public ClientServiceProviderFavorite() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
