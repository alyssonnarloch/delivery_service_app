package com.app.narlocks.delivery_service_app.model;

import java.io.Serializable;

public class State implements Serializable {

    private int id;
    private String name;
    private String initial;

    public State() {

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

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }
}
