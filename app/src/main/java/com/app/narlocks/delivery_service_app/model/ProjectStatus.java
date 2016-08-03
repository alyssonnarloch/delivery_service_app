package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;

public class ProjectStatus implements Serializable {

    private int id;
    private String name;

    public ProjectStatus() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
