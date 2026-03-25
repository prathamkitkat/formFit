package com.pm.formfitbackend.user.dto;

public class UserProfileDTO {
    private String username;
    private String email;
    private String weightUnit;

    public UserProfileDTO(String username, String email, String weightUnit) {
        this.username = username;
        this.email = email;
        this.weightUnit = weightUnit;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getWeightUnit() { return weightUnit; }
}
