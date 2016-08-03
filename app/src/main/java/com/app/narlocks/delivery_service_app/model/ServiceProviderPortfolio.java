package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;

public class ServiceProviderPortfolio implements Serializable {
    private int id;
    private String image;
    private int serviceProviderId;

    public ServiceProviderPortfolio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }
}
