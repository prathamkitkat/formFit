package com.pm.formfitbackend.security.dto;

import jakarta.validation.constraints.*;

public class AuthRequestDTO {

    public interface Login {
    }

    public interface Register {
    }

    @NotBlank(groups = {Login.class, Register.class})
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @NotBlank(groups = {Login.class, Register.class})
    private String password;

    @NotBlank(groups = Register.class)
    private String username;


    @NotBlank(groups = Register.class)
    private String timezone;
}

    // getters/setters
