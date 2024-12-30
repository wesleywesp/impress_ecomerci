package impress.weasp.service;

import impress.weasp.model.PendingAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Map<String, PendingAdmin> pendingAdmins = new ConcurrentHashMap<>();
    private final Map<String, String> otps = new ConcurrentHashMap<>();
    private final EmailService emailService;

    public void sendOtp(String email, String name, String encodedPassword) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otps.put(email, otp);

        // Armazena o PendingAdmin
        pendingAdmins.put(email, new PendingAdmin(name, encodedPassword));

        // Enviar email com OTP
        String subject = "Seu código OTP";
        String message = String.format(
                "<p>Olá %s,</p><p>Seu código OTP é: <b>%s</b>.</p><p>Use-o para concluir o cadastro.</p>",
                name, otp
        );

        try {
            emailService.sendVerificationOtpEmail(email, otp, subject, message);
        } catch (Exception e) {
            System.out.println("Erro ao enviar email com OTP: " + e.getMessage());
        }

        System.out.println("OTP enviado para " + email + ": " + otp); // Debug
    }

    public boolean validateOtp(String email, String otp) {
        return otps.containsKey(email) && otps.get(email).equals(otp);
    }

    public PendingAdmin getPendingAdmin(String email) {
        return pendingAdmins.get(email);
    }

    public void removePendingAdmin(String email) {
        otps.remove(email);
        pendingAdmins.remove(email);
    }
}



