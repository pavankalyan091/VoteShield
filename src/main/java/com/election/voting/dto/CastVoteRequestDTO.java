package com.election.voting.dto;

import jakarta.validation.constraints.NotNull;

public class CastVoteRequestDTO {
    @NotNull(message = "Voter ID is required")
    private Long voterId;

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    public Long getVoterId()             { return voterId; }
    public void setVoterId(Long v)       { this.voterId = v; }
    public Long getCandidateId()         { return candidateId; }
    public void setCandidateId(Long v)   { this.candidateId = v; }
}
