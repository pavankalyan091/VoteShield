package com.election.voting.service;

import com.election.voting.dto.LoginRequestDTO;
import com.election.voting.dto.UserResponseDTO;
import com.election.voting.entity.User;
import com.election.voting.exception.VotingException;
import com.election.voting.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO login(LoginRequestDTO request, String expectedRole) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new VotingException("No account found with this email."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new VotingException("Incorrect password.");
        }

        if (!user.getRole().equals(expectedRole)) {
            throw new VotingException("Access denied for this portal.");
        }

        UserResponseDTO dto = toDTO(user);
        return dto;
    }

    private UserResponseDTO toDTO(User u) {
        UserResponseDTO dto = new UserResponseDTO(
            u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.isHasVoted()
        );
        dto.setVoterId(u.getVoterId());
        return dto;
    }
}
