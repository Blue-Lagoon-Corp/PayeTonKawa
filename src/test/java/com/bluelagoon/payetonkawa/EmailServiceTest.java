package com.bluelagoon.payetonkawa;

import com.bluelagoon.payetonkawa.mail.interfaces.EmailInterface;
import com.bluelagoon.payetonkawa.mail.services.EmailService;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmailServiceTest {
    private EmailInterface emailInterface;

    @Before
    public void before() {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        this.emailInterface = new EmailService(javaMailSender);
    }

    @Test
    public void sendAndReturnTrue(){
        String recipient = "oguzhan.kilic@epsi.fr";
        assertTrue(emailInterface.sendEmail(recipient));
    }
}
