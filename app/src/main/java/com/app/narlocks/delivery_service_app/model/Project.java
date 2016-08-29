package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project implements Serializable {
    private int id;
    private String title;
    private String description;
    private Date startAt;
    private Date endAt;
    private String serviceProviderEvaluation;
    private String clientEvaluation;
    private Integer serviceProviderQualification;
    private Integer clientQualification;
    private Client client;
    private ServiceProvider serviceProvider;
    private City city;
    private String zipCode;
    private String address;
    private int number;
    private ProjectStatus status;
    private List<ProjectPortfolio> portfolio;

    public static final int AWATING = 1;
    public static final int REFUSED = 2;
    public static final int EXECUTION = 3;
    public static final int FINISHED = 4;

    public Project(int id) {
        this.portfolio = new ArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getServiceProviderEvaluation() {
        return serviceProviderEvaluation;
    }

    public void setServiceProviderEvaluation(String serviceProviderEvaluation) {
        this.serviceProviderEvaluation = serviceProviderEvaluation;
    }

    public String getClientEvaluation() {
        return clientEvaluation;
    }

    public void setClientEvaluation(String clientEvaluation) {
        this.clientEvaluation = clientEvaluation;
    }

    public Integer getServiceProviderQualification() {
        return serviceProviderQualification;
    }

    public void setServiceProviderQualification(Integer serviceProviderQualification) {
        this.serviceProviderQualification = serviceProviderQualification;
    }

    public Integer getClientQualification() {
        return clientQualification;
    }

    public void setClientQualification(Integer clientQualification) {
        this.clientQualification = clientQualification;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<ProjectPortfolio> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<ProjectPortfolio> portfolio) {
        this.portfolio = portfolio;
    }
}
