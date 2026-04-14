package com.election.voting.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVoterCredentials(String toEmail, String voterName,
                                     String voterId, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("VoteShield - Your Voter Credentials");

            // Plain text only — HTML తర్వాత add చేద్దాం
            String text =
                    "Hello " + voterName + ",\n\n" +
                            "You have been registered as a voter.\n\n" +
                            "Your Login Credentials:\n" +
                            "------------------------\n" +
                            "Email    : " + toEmail + "\n" +
                            "Voter ID : " + voterId + "\n" +
                            "Password : " + password + "\n" +
                            "------------------------\n\n" +
                            "Login: http://localhost:3000/voter/login\n\n" +
                            "Keep these credentials safe. Each voter can only vote once.\n\n" +
                            "- VoteShield Team";

            helper.setText(text, false);
            mailSender.send(message);
            System.out.println("Email sent to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Email failed for " + toEmail + ": " + e.getMessage());
        }
    }

    private String buildHtml(String name, String email, String voterId, String password) {
        return """
<!DOCTYPE html><html><head><meta charset="UTF-8"/>
<style>
body{font-family:Arial,sans-serif;background:#f4f6fb;margin:0;padding:0}
.wrap{max-width:580px;margin:40px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,.10)}
.hdr{background:linear-gradient(135deg,#4361EE,#7C3AED);padding:36px 40px;text-align:center}
.hdr h1{color:#fff;font-size:26px;margin:0}
.hdr p{color:rgba(255,255,255,.8);margin:8px 0 0;font-size:14px}
.body{padding:36px 40px}
.box{background:#F4F6FB;border:2px dashed rgba(67,97,238,.3);border-radius:12px;padding:24px;margin:20px 0}
.lbl{font-size:11px;font-weight:700;color:#9AABC2;text-transform:uppercase;letter-spacing:.08em;margin-bottom:14px}
.row{display:flex;justify-content:space-between;align-items:center;padding:8px 0;border-bottom:1px solid #E4E9F2}
.row:last-child{border-bottom:none}
.key{font-size:13px;color:#5B6B8A;font-weight:600}
.val{font-size:14px;color:#0D1B3E;font-weight:700;background:#fff;padding:4px 10px;border-radius:6px;border:1px solid #E4E9F2;font-family:monospace}
.vid{font-size:18px;letter-spacing:3px;color:#4361EE}
.step{display:flex;align-items:flex-start;gap:12px;margin-bottom:10px}
.num{width:24px;height:24px;border-radius:50%;background:#4361EE;color:#fff;font-size:12px;font-weight:700;display:flex;align-items:center;justify-content:center;flex-shrink:0}
.cta{text-align:center;margin:28px 0}
.cta a{background:linear-gradient(135deg,#4361EE,#3451DB);color:#fff;padding:14px 32px;border-radius:10px;text-decoration:none;font-weight:700;font-size:15px;display:inline-block}
.warn{background:#FFFBEB;border:1px solid #FDE68A;border-radius:8px;padding:12px 16px;margin:16px 0;font-size:13px;color:#92400E}
.ft{background:#F8FAFF;padding:20px 40px;text-align:center;font-size:12px;color:#9AABC2;border-top:1px solid #E4E9F2}
</style></head><body>
<div class="wrap">
  <div class="hdr"><h1>🛡️ VoteShield</h1><p>Your Official Voter Credentials</p></div>
  <div class="body">
    <p style="font-size:16px;color:#0D1B3E">Hello <strong>%s</strong>,</p>
    <p style="font-size:14px;color:#5B6B8A;line-height:1.7;margin:10px 0 0">You have been registered as a voter. Use the credentials below to login and cast your vote.</p>
    <div class="box">
      <p class="lbl">Your Login Credentials</p>
      <div class="row"><span class="key">📧 Email</span><span class="val">%s</span></div>
      <div class="row"><span class="key">🪪 Voter ID</span><span class="val vid">%s</span></div>
      <div class="row"><span class="key">🔒 Password</span><span class="val">%s</span></div>
    </div>
    <div class="step"><div class="num">1</div><p style="font-size:14px;color:#5B6B8A">Visit <strong>http://localhost:3000/voter/login</strong></p></div>
    <div class="step"><div class="num">2</div><p style="font-size:14px;color:#5B6B8A">Enter your email and password</p></div>
    <div class="step"><div class="num">3</div><p style="font-size:14px;color:#5B6B8A">Select a candidate and cast your vote</p></div>
    <div class="cta"><a href="http://localhost:3000/voter/login">Login &amp; Vote Now →</a></div>
    <div class="warn">⚠️ Keep these credentials private. Each voter can only vote <strong>once</strong>. Your vote is cryptographically secured.</div>
  </div>
  <div class="ft">VoteShield — Secure Online Election System<br/>This is an automated message. Do not reply.</div>
</div></body></html>
""".formatted(name, email, voterId, password);
    }
}
