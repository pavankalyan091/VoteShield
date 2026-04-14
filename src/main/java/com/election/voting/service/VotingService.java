package com.election.voting.service;

import com.election.voting.dto.CastVoteRequestDTO;
import com.election.voting.entity.*;
import com.election.voting.exception.VotingException;
import com.election.voting.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class VotingService {

    private final CandidateRepository candidateRepository;
    private final UserRepository      userRepository;
    private final VoteRepository      voteRepository;
    private final HashService         hashService;

    public VotingService(CandidateRepository c, UserRepository u,
                         VoteRepository v, HashService h) {
        this.candidateRepository = c;
        this.userRepository      = u;
        this.voteRepository      = v;
        this.hashService         = h;
    }

    @Transactional
    public String castVote(CastVoteRequestDTO request) {
        Long userId      = request.getVoterId();
        Long candidateId = request.getCandidateId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new VotingException("Voter not found."));

        if (voteRepository.existsByUserId(userId)) {
            throw new VotingException("You have already cast your vote.");
        }

        Candidate candidate = candidateRepository.findById(candidateId)
            .orElseThrow(() -> new VotingException("Candidate not found."));

        // Update vote count
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        candidateRepository.save(candidate);

        // Mark user as voted
        user.setHasVoted(true);
        userRepository.save(user);

        // Save vote WITH cryptographic hash
        LocalDateTime now = LocalDateTime.now();
        String voteHash   = hashService.generateVoteHash(userId, candidateId, now);

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setCandidate(candidate);
        vote.setVotedAt(now);
        vote.setVoteHash(voteHash);
        voteRepository.save(vote);

        return "Vote successfully cast for " + candidate.getName();
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();

    }

    public List<Candidate> getResults() {
        List<Candidate> list = candidateRepository.findAll();
        list.sort((a, b) -> Integer.compare(b.getVoteCount(), a.getVoteCount()));
        return list;
    }
}
