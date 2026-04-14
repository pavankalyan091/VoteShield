package com.election.voting;

import com.election.voting.entity.Candidate;
import com.election.voting.entity.User;
import com.election.voting.repository.CandidateRepository;
import com.election.voting.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository      userRepository;
    private final CandidateRepository candidateRepository;

    public DataInitializer(UserRepository u, CandidateRepository c) {
        this.userRepository      = u;
        this.candidateRepository = c;
    }

    @Override
    public void run(String... args) {
        // Seed admin account
        if (!userRepository.existsByEmail("admin@voteshield.com")) {
            userRepository.save(new User("System Administrator", "admin@voteshield.com", "admin@123", "ADMIN"));
            System.out.println("Admin account created: admin@voteshield.com / admin@123");
        }

        // Seed sample candidates
        if (candidateRepository.count() == 0) {
            candidateRepository.saveAll(List.of(
                new Candidate("Ravi Kumar",   "National Progressive Party", "🌟"),
                new Candidate("Priya Sharma", "Democratic Alliance",        "🌺"),
                new Candidate("Mohammed Ali", "United People's Front",      "🕊️"),
                new Candidate("Sunita Reddy", "Green Future Party",         "🌿")
            ));
            System.out.println("Sample candidates seeded.");
        }
    }
}
