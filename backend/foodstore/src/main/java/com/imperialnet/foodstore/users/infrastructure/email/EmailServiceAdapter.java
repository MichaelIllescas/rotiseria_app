package com.imperialnet.foodstore.users.infrastructure.email;

import com.imperialnet.foodstore.users.application.ports.out.EmailServicePort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmailServiceAdapter implements EmailServicePort {

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;


    @Override
    public void send(String to, String subject, String templateName, Object model) {
        try {
            String htmlBody = templateService.render(templateName, (Map<String, Object>) model);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("info@imperial-net.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo", e);
        }
    }
}
