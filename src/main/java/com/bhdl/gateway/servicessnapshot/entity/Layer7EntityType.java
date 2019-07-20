package com.bhdl.gateway.servicessnapshot.entity;

public enum Layer7EntityType {
    POLICY("policies"),
    SERVICE("services");
    private String resource;

    Layer7EntityType(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
