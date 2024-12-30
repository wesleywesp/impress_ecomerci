package impress.weasp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) {
        try {
            // Criação de uma mensagem MIME
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Configuração do email
            helper.setSubject(subject);
            helper.setText(text, true); // Define o conteúdo como HTML
            helper.setTo(userEmail);

            // Envio do email
            javaMailSender.send(mimeMessage);
            System.out.println("Email enviado para: " + userEmail); // Debug

        } catch (MessagingException | MailException e) {
            System.out.println("Falha ao enviar email -- " + e.getMessage());
            throw new RuntimeException("Falha ao enviar email para " + userEmail, e);
        }
    }
}

