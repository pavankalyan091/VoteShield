package com.election.voting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String partyName;

    private String partySymbol;

    private int voteCount = 0;

    public Candidate() {}

    public Candidate(String name, String partyName, String partySymbol) {
        this.name        = name;
        this.partyName   = partyName;
        this.partySymbol = partySymbol;
        this.voteCount   = 0;
    }

    public Long getId()                    { return id; }
    public String getName()                { return name; }
    public void setName(String v)          { this.name = v; }
    public String getPartyName()           { return partyName; }
    public void setPartyName(String v)     { this.partyName = v; }
    public String getPartySymbol()         { return partySymbol; }
    public void setPartySymbol(String v)   { this.partySymbol = v; }
    public int getVoteCount()              { return voteCount; }
    public void setVoteCount(int v)        { this.voteCount = v; }
}
