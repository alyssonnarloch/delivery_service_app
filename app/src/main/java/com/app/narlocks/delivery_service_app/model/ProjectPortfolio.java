package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;

public class ProjectPortfolio implements Serializable {
    private int id;
    private String image;
    private Boolean approved;
    private int projectId;

    public ProjectPortfolio() {
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

    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
