package com.election.voting.controller;

import com.election.voting.dto.*;
import com.election.voting.entity.*;
import com.election.voting.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── Voter Management ──────────────────────────────────────────────
    @PostMapping("/voters")
    public ResponseEntity<UserResponseDTO> createVoter(
            @Valid @RequestBody CreateVoterRequestDTO req) {
        return new ResponseEntity<>(adminService.createVoter(req), HttpStatus.CREATED);
    }

    @PutMapping("/voters/{id}")
    public ResponseEntity<UserResponseDTO> updateVoter(
            @PathVariable Long id, @RequestBody UpdateVoterRequestDTO req) {
        return ResponseEntity.ok(adminService.updateVoter(id, req));
    }

    @DeleteMapping("/voters/{id}")
    public ResponseEntity<String> deleteVoter(@PathVariable Long id) {
        adminService.deleteVoter(id);
        return ResponseEntity.ok("Voter deleted successfully.");
    }

    @GetMapping("/voters")
    public ResponseEntity<List<User>> getAllVoters() {
        return ResponseEntity.ok(adminService.getAllVoters());
    }

    // ── Candidate Management ──────────────────────────────────────────
    @PostMapping("/candidates")
    public ResponseEntity<Candidate> createCandidate(
            @Valid @RequestBody CreateCandidateRequestDTO req) {
        return new ResponseEntity<>(adminService.createCandidate(req), HttpStatus.CREATED);
    }

    @PutMapping("/candidates/{id}")
    public ResponseEntity<Candidate> updateCandidate(
            @PathVariable Long id, @RequestBody UpdateCandidateRequestDTO req) {
        return ResponseEntity.ok(adminService.updateCandidate(id, req));
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
        adminService.deleteCandidate(id);
        return ResponseEntity.ok("Candidate deleted successfully.");
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(adminService.getAllCandidates());
    }

    // ── Stats & Integrity ─────────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<AdminService.ElectionStats> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    /**
     * Verify all votes are tamper-proof using SHA-256 hashes.
     * Returns: totalVotes, validVotes, tamperedVotes, electionIntact flag
     */
    @GetMapping("/verify-integrity")
    public ResponseEntity<VoteVerificationDTO> verifyIntegrity() {
        return ResponseEntity.ok(adminService.verifyElectionIntegrity());
    }
}
