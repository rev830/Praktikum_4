package com.example.praktikum.dto;

import jakarta.validation.constraints.NotEmpty;


public class PostTaskRequest {

    @NotEmpty(message = "Beschreibung darf nicht leer sein")
    private String description;

    public PostTaskRequest() {
        // Standard-Konstruktor für Frameworks, Tests etc.
    }

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
