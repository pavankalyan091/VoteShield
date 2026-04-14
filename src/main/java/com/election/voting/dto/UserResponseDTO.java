package com.election.voting.dto;

public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String voterId;
    private String role;
    private boolean hasVoted;

    public UserResponseDTO() {}
    public UserResponseDTO(Long id, String username, String email,
                            String role, boolean hasVoted) {
        this.id       = id;
        this.username = username;
        this.email    = email;
        this.role     = role;
        this.hasVoted = hasVoted;
    }

    public Long getId()              { return id; }
    public String getUsername()      { return username; }
    public String getEmail()         { return email; }
    public String getVoterId()       { return voterId; }
    public void setVoterId(String v) { this.voterId = v; }
    public String getRole()          { return role; }
    public boolean isHasVoted()      { return hasVoted; }
}
