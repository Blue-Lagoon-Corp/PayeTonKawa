package com.bluelagoon.payetonkawa.mail.services;

import com.bluelagoon.payetonkawa.mail.interfaces.EmailInterface;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailInterface {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendEmail(String setTo) {
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(setTo);
            String htmlMessage = "<p>Bonjour</p>"+"<br>"+
                    "<p>Vous trouverez ci dessous votre qr code à scanner pour vous connecter</p>";
            mimeMessage.setContent(htmlMessage, "text/html");
            String subject = "PayeTonKawa - Votre QR Code de connexion";
            mimeMessageHelper.setSubject(subject);
            javaMailSender.send(mimeMessage);
            return true;
        }

        catch (MessagingException e) {

            return false;
        }
    }
}
