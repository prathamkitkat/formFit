package com.pm.formfitbackend.user.service;

import com.pm.formfitbackend.user.dto.*;
import com.pm.formfitbackend.user.entity.User;
import com.pm.formfitbackend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserProfileDTO getProfile(Long userId) {
        User user = findById(userId);
        return new UserProfileDTO(user.getUsername(), user.getEmail(), user.getWeightUnit());
    }

    public void updateUsername(Long userId, UpdateUsernameDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        User user = findById(userId);
        user.setUsername(dto.getUsername());
        userRepository.save(user);
    }

    public void updateEmail(Long userId, UpdateEmailDTO dto) {

            String newEmail = dto.getEmail();
// Case 1: Same as existing email
        if (userRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("Email already taken: " + newEmail);
        }
        User user = findById(userId);
        if (newEmail.equals(user.getEmail())) {
            throw new RuntimeException("New email cannot be same as current email");
        }

        user.setEmail(dto.getEmail());
        userRepository.save(user);
    }

    public void updatePassword(Long userId, UpdatePasswordDTO dto) {
        User user = findById(userId);
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public void updateWeightUnit(Long userId, UpdateWeightUnitDTO dto) {
        String unit = dto.getWeightUnit();
        if (!"KG".equals(unit) && !"LB".equals(unit)) {
            throw new RuntimeException("Invalid weight unit. Must be KG or LB");
        }
        User user = findById(userId);
        user.setWeightUnit(unit);
        userRepository.save(user);
    }


}