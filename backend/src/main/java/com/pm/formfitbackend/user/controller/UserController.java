package com.pm.formfitbackend.user.controller;

import com.pm.formfitbackend.user.dto.*;
import com.pm.formfitbackend.user.service.UserService;
import com.pm.formfitbackend.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getProfile(userDetails.getUserId()));
    }

    @PatchMapping("/username")
    public ResponseEntity<Void> updateUsername(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateUsernameDTO dto
    ) {
        userService.updateUsername(userDetails.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/email")
    public ResponseEntity<Void> updateEmail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateEmailDTO dto
    ) {
        userService.updateEmail(userDetails.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdatePasswordDTO dto
    ) {
        userService.updatePassword(userDetails.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/weight-unit")
    public ResponseEntity<Void> updateWeightUnit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateWeightUnitDTO dto
    ) {
        userService.updateWeightUnit(userDetails.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }
}