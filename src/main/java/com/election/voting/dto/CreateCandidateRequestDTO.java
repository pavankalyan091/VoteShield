package com.election.voting.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCandidateRequestDTO {
    @NotBlank(message = "Candidate name is required")
    private String name;

    @NotBlank(message = "Party name is required")
    private String partyName;

    private String partySymbol;

    public String getName()              { return name; }
    public void setName(String v)        { this.name = v; }
    public String getPartyName()         { return partyName; }
    public void setPartyName(String v)   { this.partyName = v; }
    public String getPartySymbol()       { return partySymbol; }
    public void setPartySymbol(String v) { this.partySymbol = v; }
}
