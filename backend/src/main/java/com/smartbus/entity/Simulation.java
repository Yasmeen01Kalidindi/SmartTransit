package com.smartbus.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "simulations")
public class Simulation {

    @Id
    private String id;

    private String scenario;

    private String value;

    private String beforeResult;

    private String afterResult;

    public Simulation() {
    }

    public Simulation(String id, String scenario, String value,
                      String beforeResult, String afterResult) {
        this.id = id;
        this.scenario = scenario;
        this.value = value;
        this.beforeResult = beforeResult;
        this.afterResult = afterResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBeforeResult() {
        return beforeResult;
    }

    public void setBeforeResult(String beforeResult) {
        this.beforeResult = beforeResult;
    }

    public String getAfterResult() {
        return afterResult;
    }

    public void setAfterResult(String afterResult) {
        this.afterResult = afterResult;
    }
}