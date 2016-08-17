package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;

public class ServiceType implements Serializable {

    private int id;
    private String name;
    private boolean selected;

    public ServiceType() {
    }

    public ServiceType(int id, String name, boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
