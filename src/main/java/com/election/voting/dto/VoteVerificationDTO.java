package com.election.voting.dto;

public class VoteVerificationDTO {
    private long totalVotes;
    private long validVotes;
    private long tamperedVotes;
    private boolean electionIntact;
    private String message;

    public VoteVerificationDTO(long total, long valid, long tampered) {
        this.totalVotes    = total;
        this.validVotes    = valid;
        this.tamperedVotes = tampered;
        this.electionIntact = (tampered == 0);
        this.message = (tampered == 0)
            ? "✅ Election integrity verified. All " + total + " votes are authentic."
            : "🚨 WARNING: " + tampered + " vote(s) may have been tampered!";
    }

    public long getTotalVotes()     { return totalVotes; }
    public long getValidVotes()     { return validVotes; }
    public long getTamperedVotes()  { return tamperedVotes; }
    public boolean isElectionIntact(){ return electionIntact; }
    public String getMessage()      { return message; }
}
