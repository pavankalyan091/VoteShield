package com.election.voting.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    // Unique voter ID: VS-XXXXXXXX format — auto generated
    @Column(unique = true)
    private String voterId;

    // VOTER or ADMIN
    @Column(nullable = false)
    private String role = "VOTER";

    @Column(nullable = false)
    private boolean hasVoted = false;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Vote vote;

    public User() {}

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    public Long getId()                   { return id; }
    public String getUsername()           { return username; }
    public void setUsername(String v)     { this.username = v; }
    public String getPassword()           { return password; }
    public void setPassword(String v)     { this.password = v; }
    public String getEmail()              { return email; }
    public void setEmail(String v)        { this.email = v; }
    public String getVoterId()            { return voterId; }
    public void setVoterId(String v)      { this.voterId = v; }
    public String getRole()               { return role; }
    public void setRole(String v)         { this.role = v; }
    public boolean isHasVoted()           { return hasVoted; }
    public void setHasVoted(boolean v)    { this.hasVoted = v; }
    public Vote getVote()                 { return vote; }
    public void setVote(Vote v)           { this.vote = v; }
}
