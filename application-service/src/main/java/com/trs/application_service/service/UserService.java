package com.trs.application_service.service;

import com.trs.application_service.dto.RegisterRequest;
import com.trs.application_service.dto.UserResponse;
import com.trs.application_service.exception.DuplicateResourceException;
import com.trs.application_service.exception.ResourceNotFoundException;
import com.trs.application_service.model.UserAccount;
import com.trs.application_service.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Email tidak boleh kosong");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }
        if (userAccountRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email sudah terdaftar");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(request.email().trim().toLowerCase());
        userAccount.setPassword(passwordEncoder.encode(request.password()));

        UserAccount savedUser = userAccountRepository.save(userAccount);
        return new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getCreatedAt());
    }

    public UserResponse getByEmail(String email) {
        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        return new UserResponse(userAccount.getId(), userAccount.getEmail(), userAccount.getCreatedAt());
    }
}
