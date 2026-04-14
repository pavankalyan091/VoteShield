package com.election.voting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateVoterRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // No password field — system auto-generates it
    public String getFullName()        { return fullName; }
    public void setFullName(String v)  { this.fullName = v; }
    public String getEmail()           { return email; }
    public void setEmail(String v)     { this.email = v; }
}
