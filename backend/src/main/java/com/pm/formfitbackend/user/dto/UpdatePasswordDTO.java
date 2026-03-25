// UpdatePasswordDTO.java
package com.pm.formfitbackend.user.dto;

public class UpdatePasswordDTO {
    private String currentPassword;
    private String newPassword;
    public String getCurrentPassword() { return currentPassword; }
    public String getNewPassword() { return newPassword; }
}