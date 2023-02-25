package com.bluelagoon.payetonkawa.mail.impl;

import com.bluelagoon.payetonkawa.mail.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public static final String SENDER = "noreply.spree@gmail.com";

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendEmail(String setTo) {
        var mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(SENDER);
            mimeMessageHelper.setTo(setTo);
            var htmlMessage = "<p>Bonjour</p>"+"<br>"+
                    "<p>Vous trouverez ci dessous votre qr code à scanner pour vous connecter</p>";
            mimeMessage.setContent(htmlMessage, "text/html");
            var subject = "PayeTonKawa - Votre QR Code de connexion";
            mimeMessageHelper.setSubject(subject);
            javaMailSender.send(mimeMessage);
            return true;
        }

        catch (MessagingException e) {

            return false;
        }
    }
}
