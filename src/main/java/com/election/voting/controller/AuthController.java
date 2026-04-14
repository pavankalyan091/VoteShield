package com.election.voting.controller;

import com.election.voting.dto.LoginRequestDTO;
import com.election.voting.dto.UserResponseDTO;
import com.election.voting.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Admin login — only ADMIN role allowed
    @PostMapping("/admin/login")
    public ResponseEntity<UserResponseDTO> adminLogin(@Valid @RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(authService.login(req, "ADMIN"));
    }

    // Voter login — only VOTER role allowed
    @PostMapping("/voter/login")
    public ResponseEntity<UserResponseDTO> voterLogin(@Valid @RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(authService.login(req, "VOTER"));
    }
}
