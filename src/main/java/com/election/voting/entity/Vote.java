package com.election.voting.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    private LocalDateTime votedAt = LocalDateTime.now();

    // SHA-256 hash for tamper-proof verification
    // hash = SHA256(userId + candidateId + votedAt + secretSalt)
    @Column(nullable = false, unique = true)
    private String voteHash;

    public Vote() {}

    public Long getId()                     { return id; }
    public User getUser()                   { return user; }
    public void setUser(User v)             { this.user = v; }
    public Candidate getCandidate()         { return candidate; }
    public void setCandidate(Candidate v)   { this.candidate = v; }
    public LocalDateTime getVotedAt()       { return votedAt; }
    public void setVotedAt(LocalDateTime v) { this.votedAt = v; }
    public String getVoteHash()             { return voteHash; }
    public void setVoteHash(String v)       { this.voteHash = v; }
}
