package com.app.narlocks.delivery_service_app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceProvider extends User {
    private String experienceDescription;
    private boolean available;
    private List<Integer> serviceTypeIds;
    private List<Integer> occupationAreaIds;
    private List<String> profilePortfolioSrc;
    private Set<ServiceType> serviceTypes;
    private Set<City> occupationAreas;
    private Set<ServiceProviderPortfolio> portfolio;
    private double qualificationAvg;

    public ServiceProvider() {
        this.serviceTypeIds = new ArrayList();
        this.occupationAreaIds = new ArrayList();
        this.profilePortfolioSrc = new ArrayList();
        this.serviceTypes = new HashSet();
        this.occupationAreas = new HashSet();
        this.portfolio = new HashSet();
    }

    public String getExperienceDescription() {
        return experienceDescription;
    }

    public void setExperienceDescription(String experienceDescription) {
        this.experienceDescription = experienceDescription;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<Integer> getServiceTypeIds() {
        return serviceTypeIds;
    }

    public void setServiceTypeIds(List<Integer> serviceTypeIds) {
        this.serviceTypeIds = serviceTypeIds;
    }

    public List<Integer> getOccupationAreaIds() {
        return occupationAreaIds;
    }

    public void setOccupationAreaIds(List<Integer> occupationAreaIds) {
        this.occupationAreaIds = occupationAreaIds;
    }

    public List<String> getProfilePortfolioSrc() {
        return profilePortfolioSrc;
    }

    public void setProfilePortfolioSrc(List<String> profilePortfolioSrc) {
        this.profilePortfolioSrc = profilePortfolioSrc;
    }

    public Set<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(Set<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Set<City> getOccupationAreas() {
        return occupationAreas;
    }

    public void setOccupationAreas(Set<City> occupationAreas) {
        this.occupationAreas = occupationAreas;
    }

    public Set<ServiceProviderPortfolio> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Set<ServiceProviderPortfolio> portfolio) {
        this.portfolio = portfolio;
    }

    public double getQualificationAvg() {
        return qualificationAvg;
    }

    public void setQualificationAvg(double qualificationAvg) {
        this.qualificationAvg = qualificationAvg;
    }

    public void addServiceType(ServiceType serviceType) {
        this.serviceTypes.add(serviceType);
    }

    public void addProfilePortfolioSrc(String src) {
        this.profilePortfolioSrc.add(src);
    }

    public void addOccupationAreaId(int cityId) {
        if (!this.occupationAreaIds.contains(cityId)) {
            this.occupationAreaIds.add(cityId);
        }
    }

    public void addServiceTypeId(int serviceTypeId) {
        if (!this.serviceTypeIds.contains(serviceTypeId)) {
            this.serviceTypeIds.add(serviceTypeId);
        }
    }
}
