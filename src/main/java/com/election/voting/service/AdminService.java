package com.election.voting.service;

import com.election.voting.dto.*;
import com.election.voting.entity.*;
import com.election.voting.exception.VotingException;
import com.election.voting.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository      userRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository      voteRepository;
    private final EmailService        emailService;
    private final HashService         hashService;

    public AdminService(UserRepository u, CandidateRepository c,
                        VoteRepository v, EmailService e, HashService h) {
        this.userRepository      = u;
        this.candidateRepository = c;
        this.voteRepository      = v;
        this.emailService        = e;
        this.hashService         = h;
    }

    // ── Voter Management ──────────────────────────────────────────────

    /**
     * Admin only enters name + email.
     * System auto-generates: Voter ID + secure password.
     * Email sent automatically to voter — admin never sees password.
     */
    public UserResponseDTO createVoter(CreateVoterRequestDTO req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new VotingException("This email is already registered.");
        }

        // Auto-generate unique Voter ID
        String voterId;
        do { voterId = hashService.generateVoterId(); }
        while (userRepository.existsByVoterId(voterId));

        // Auto-generate secure password — admin never sees this
        String autoPassword = hashService.generateSecurePassword();

        User voter = new User();
        voter.setUsername(req.getFullName());
        voter.setEmail(req.getEmail());
        voter.setPassword(autoPassword);
        voter.setVoterId(voterId);
        voter.setRole("VOTER");
        voter.setHasVoted(false);

        User saved = userRepository.save(voter);

        // Send credentials email to voter
        emailService.sendVoterCredentials(saved.getEmail(), saved.getUsername(),
                                          voterId, autoPassword);

        return toDTO(saved);
    }

    /**
     * Admin can edit voter name and/or email.
     * Password cannot be changed by admin — voter owns it.
     * Cannot edit if voter has already voted.
     */
    public UserResponseDTO updateVoter(Long id, UpdateVoterRequestDTO req) {
        User voter = userRepository.findById(id)
            .orElseThrow(() -> new VotingException("Voter not found."));

        if (!voter.getRole().equals("VOTER")) {
            throw new VotingException("Cannot edit admin account.");
        }

        if (req.getFullName() != null && !req.getFullName().isBlank()) {
            voter.setUsername(req.getFullName());
        }
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            if (!req.getEmail().equals(voter.getEmail()) &&
                userRepository.existsByEmail(req.getEmail())) {
                throw new VotingException("This email is already in use.");
            }
            voter.setEmail(req.getEmail());
        }

        return toDTO(userRepository.save(voter));
    }

    public void deleteVoter(Long id) {
        User voter = userRepository.findById(id)
            .orElseThrow(() -> new VotingException("Voter not found."));
        if (voter.getRole().equals("ADMIN")) {
            throw new VotingException("Cannot delete admin account.");
        }
        if (voter.isHasVoted()) {
            throw new VotingException("Cannot delete a voter who has already voted.");
        }
        userRepository.delete(voter);
    }

    public List<User> getAllVoters() {
        return userRepository.findByRole("VOTER");
    }

    // ── Candidate Management ──────────────────────────────────────────

    public Candidate createCandidate(CreateCandidateRequestDTO req) {
        String symbol = (req.getPartySymbol() != null && !req.getPartySymbol().isBlank())
            ? req.getPartySymbol() : "🏛️";
        return candidateRepository.save(new Candidate(req.getName(), req.getPartyName(), symbol));
    }

    /**
     * Admin can edit candidate details.
     * voteCount is NOT editable — system controlled only.
     */
    public Candidate updateCandidate(Long id, UpdateCandidateRequestDTO req) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new VotingException("Candidate not found."));

        if (req.getName() != null && !req.getName().isBlank())
            c.setName(req.getName());
        if (req.getPartyName() != null && !req.getPartyName().isBlank())
            c.setPartyName(req.getPartyName());
        if (req.getPartySymbol() != null && !req.getPartySymbol().isBlank())
            c.setPartySymbol(req.getPartySymbol());

        return candidateRepository.save(c);
    }

    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id))
            throw new VotingException("Candidate not found.");
        candidateRepository.deleteById(id);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // ── Stats ─────────────────────────────────────────────────────────

    public ElectionStats getStats() {
        List<User> voters          = userRepository.findByRole("VOTER");
        List<Candidate> candidates = candidateRepository.findAll();
        long votedCount            = voters.stream().filter(User::isHasVoted).count();
        int totalVotes             = candidates.stream().mapToInt(Candidate::getVoteCount).sum();
        double turnout             = voters.isEmpty() ? 0.0 : (votedCount * 100.0 / voters.size());
        return new ElectionStats(voters.size(), candidates.size(),
                                 (int) votedCount, totalVotes,
                                 Math.round(turnout * 10) / 10.0);
    }

    // ── Vote Integrity Verification ───────────────────────────────────

    /**
     * All votes ki SHA-256 hash verify chestundi.
     * Tampered votes detect cheyyadam.
     * Admin-only endpoint.
     */
    public VoteVerificationDTO verifyElectionIntegrity() {
        var votes  = voteRepository.findAll();
        long valid = 0, tampered = 0;

        for (var vote : votes) {
            boolean ok = hashService.verifyVoteHash(
                vote.getUser().getId(),
                vote.getCandidate().getId(),
                vote.getVotedAt(),
                vote.getVoteHash()
            );
            if (ok) valid++; else tampered++;
        }

        return new VoteVerificationDTO(votes.size(), valid, tampered);
    }

    private UserResponseDTO toDTO(User u) {
        return new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail(),
                                   u.getRole(), u.isHasVoted());
    }

    public record ElectionStats(int totalVoters, int totalCandidates,
                                int votedCount, int totalVotes, double turnoutPct) {}
}
