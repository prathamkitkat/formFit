package com.pm.formfitbackend.security;

import com.pm.formfitbackend.security.dto.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @Validated(AuthRequestDTO.Login.class)
            @RequestBody AuthRequestDTO request
    ) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public void register(
            @Validated(AuthRequestDTO.Register.class)
            @RequestBody AuthRequestDTO request
    ) {
        authService.register(request);
    }
}