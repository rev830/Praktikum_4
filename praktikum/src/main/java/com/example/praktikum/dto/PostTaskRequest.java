package com.example.praktikum.dto;

public class PostTaskRequest {

    private String description;

    public PostTaskRequest(String description, boolean done) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
