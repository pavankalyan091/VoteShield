package com.election.voting.dto;

public class UpdateVoterRequestDTO {
    private String fullName;
    private String email;

    public String getFullName()        { return fullName; }
    public void setFullName(String v)  { this.fullName = v; }
    public String getEmail()           { return email; }
    public void setEmail(String v)     { this.email = v; }
}
