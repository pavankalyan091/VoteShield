package com.election.voting.controller;

import com.election.voting.dto.CastVoteRequestDTO;
import com.election.voting.entity.Candidate;
import com.election.voting.service.VotingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vote")
public class VotingController {

    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getCandidates() {
        return ResponseEntity.ok(votingService.getAllCandidates());
    }

    @PostMapping("/cast")
    public ResponseEntity<String> castVote(@Valid @RequestBody CastVoteRequestDTO req) {
        return ResponseEntity.ok(votingService.castVote(req));
    }

    @GetMapping("/results")
    public ResponseEntity<List<Candidate>> getResults() {
        return ResponseEntity.ok(votingService.getResults());
    }
}
