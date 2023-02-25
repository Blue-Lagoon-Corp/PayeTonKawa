package com.bluelagoon.payetonkawa.unit.mail.services;

import com.bluelagoon.payetonkawa.mail.impl.EmailServiceImpl;
import com.bluelagoon.payetonkawa.mail.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmailServiceTests {

    private EmailService emailService;

    private JavaMailSender javaMailSender;

    @Before
    public void setUp(){
        javaMailSender  = mock(JavaMailSenderImpl.class);
        emailService = new EmailServiceImpl(javaMailSender);
    }

    @Test
    public void sendEmailTestAndReturnTrue() throws MessagingException {
        System.out.println("Begin testing");
        var to = "test@example.com";
        var content = "<p>Bonjour</p>"+"<br>"+
                "<p>Vous trouverez ci dessous votre qr code à scanner pour vous connecter</p>";
        var subject = "PayeTonKawa - Votre QR Code de connexion";

        var mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(EmailServiceImpl.SENDER);
        mimeMessageHelper.setTo(to);

        mimeMessage.setContent(content, "text/html");
        mimeMessageHelper.setSubject(subject);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        var result = emailService.sendEmail(to);
        Assertions.assertTrue(result);
    }
}
