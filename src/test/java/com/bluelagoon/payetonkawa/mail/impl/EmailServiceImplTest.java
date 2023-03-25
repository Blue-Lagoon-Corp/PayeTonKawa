package com.bluelagoon.payetonkawa.mail.impl;

import com.bluelagoon.payetonkawa.mail.services.EmailService;
import com.bluelagoon.payetonkawa.qrcode.impl.QrCodeServiceImpl;
import com.bluelagoon.payetonkawa.qrcode.services.QrCodeService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    private EmailService emailService;

    private QrCodeService qrCodeService;

    private JavaMailSender javaMailSender;

    @BeforeEach
    void init(){
        javaMailSender  = mock(JavaMailSenderImpl.class);
        qrCodeService = mock(QrCodeServiceImpl.class);
        emailService = new EmailServiceImpl(javaMailSender, qrCodeService);
    }

    @Test
    void sendEmailTestAndReturnTrue() throws MessagingException, IOException, WriterException {
        System.out.println("Begin testing");
        var to = "test@example.com";
        var content = "<p>Bonjour</p>"+"<br>"+
                "<p>Vous trouverez ci dessous votre qr code à scanner pour vous connecter</p>"+
                "<img src=\"cid:qrcode.png\"></img><br/>";

        var subject = "PayeTonKawa - Votre QR Code de connexion";
        var qr = new byte[1];

        when(qrCodeService.createQR(anyString()))
                .thenReturn(qr);

        var mimeMessage = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(EmailServiceImpl.SENDER);
        mimeMessageHelper.setTo(to);

        mimeMessageHelper.setText(content, true);
        mimeMessageHelper.setSubject(subject);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        var result = emailService.sendEmail(to, "toto");

        Assertions.assertTrue(result);
    }
}
