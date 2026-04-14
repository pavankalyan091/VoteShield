package com.election.voting.service;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class HashService {

    // Secret salt — stored in application, never in DB
    // In production: move this to environment variable
    private static final String SECRET_SALT = "VoteShield@2025#SecureElection";

    /**
     * Vote ki SHA-256 hash create chestundi
     * Input: userId + candidateId + votedAt + SECRET_SALT
     * Idi DB lo tamper chesthe hash match avvadu → fraud detect avutundi
     */
    public String generateVoteHash(Long userId, Long candidateId, LocalDateTime votedAt) {
        String data = userId + "|" + candidateId + "|" + votedAt.toString() + "|" + SECRET_SALT;
        return sha256(data);
    }

    /**
     * Existing vote hash verify chestundi
     * DB lo stored hash vs recomputed hash — match ayite valid
     */
    public boolean verifyVoteHash(Long userId, Long candidateId, LocalDateTime votedAt, String storedHash) {
        String recomputed = generateVoteHash(userId, candidateId, votedAt);
        return recomputed.equals(storedHash);
    }

    /**
     * Voter ki secure random password generate chestundi
     * Admin ki kuda telidhu — direct email ki veltundi
     * Format: 3 uppercase + 3 digits + 3 lowercase + 2 special = 11 chars
     */
    public String generateSecurePassword() {
        String upper   = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lower   = "abcdefghjkmnpqrstuvwxyz";
        String digits  = "23456789";
        String special = "@#$!";

        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // Guarantee at least one of each type
        sb.append(upper.charAt(rnd.nextInt(upper.length())));
        sb.append(upper.charAt(rnd.nextInt(upper.length())));
        sb.append(digits.charAt(rnd.nextInt(digits.length())));
        sb.append(digits.charAt(rnd.nextInt(digits.length())));
        sb.append(lower.charAt(rnd.nextInt(lower.length())));
        sb.append(lower.charAt(rnd.nextInt(lower.length())));
        sb.append(lower.charAt(rnd.nextInt(lower.length())));
        sb.append(special.charAt(rnd.nextInt(special.length())));

        // Shuffle for randomness
        char[] chars = sb.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            char tmp = chars[i]; chars[i] = chars[j]; chars[j] = tmp;
        }
        return new String(chars);
    }

    /**
     * Unique Voter ID generate chestundi: VS-XXXXXXXX format
     */
    public String generateVoterId() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder("VS-");
        for (int i = 0; i < 8; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    // SHA-256 helper
    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }
}
