package com.smartbus.dto;

public class SimulationRequest {

    private String scenario;
    private String value;

    public SimulationRequest() {
    }

    public SimulationRequest(
            String scenario,
            String value) {

        this.scenario = scenario;
        this.value = value;
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
}