package com.pm.formfitbackend.template.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateTemplateNameRequest {

    @NotBlank(message = "Template name cannot be blank")
    private String name;

    public UpdateTemplateNameRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}