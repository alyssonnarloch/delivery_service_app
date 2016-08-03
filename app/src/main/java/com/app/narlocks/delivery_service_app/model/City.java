package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;

public class City implements Serializable {

    private int id;
    private String name;
    private State state;

    public City() {
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
