package com.example.praktikum.dto;

public class PatchTaskRequest {

    private String description;
    private Boolean done;

    public PatchTaskRequest(String description, Boolean done) {
        this.description = description;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
    
}
