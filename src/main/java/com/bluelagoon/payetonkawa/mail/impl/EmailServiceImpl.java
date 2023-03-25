package com.bluelagoon.payetonkawa.mail.impl;

import com.bluelagoon.payetonkawa.mail.services.EmailService;
import com.bluelagoon.payetonkawa.qrcode.services.QrCodeService;
import com.google.zxing.WriterException;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final QrCodeService qrCodeService;

    public static final String SENDER = "noreply.spree@gmail.com";

    private static final String QR_CODE_NAME = "qrcode.png";

    public EmailServiceImpl(JavaMailSender javaMailSender, QrCodeService qrCodeService) {
        this.javaMailSender = javaMailSender;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public boolean sendEmail(String setTo, String code) {
        var mimeMessage
                = javaMailSender.createMimeMessage();
        try {
            var bytesQrCode = qrCodeService.createQR(code);
            var imagePart = new MimeBodyPart();
            var imageDataSource = new ByteArrayDataSource(bytesQrCode,"image/png");

            imagePart.setDataHandler(new DataHandler(imageDataSource));
            imagePart.setHeader("Content-ID", QR_CODE_NAME);
            imagePart.setFileName(QR_CODE_NAME);

            var mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.addAttachment(QR_CODE_NAME, imageDataSource);

            mimeMessageHelper.setFrom(SENDER);
            mimeMessageHelper.setTo(setTo);

            var htmlMessage = "<p>Bonjour,</p>"+
                    "<p>Vous trouverez ci dessous votre qr code à scanner pour vous connecter</p><br>" +
                    "<img src=\"cid:qrcode.png\"></img><br/>";

            mimeMessageHelper.setText(htmlMessage, true);
            var subject = "PayeTonKawa - Votre QR Code de connexion";

            mimeMessageHelper.setSubject(subject);
            javaMailSender.send(mimeMessage);

            return true;
        }
        catch (MessagingException | IOException | WriterException e) {
            return false;
        }
    }
}
