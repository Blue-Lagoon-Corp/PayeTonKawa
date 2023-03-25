package com.bluelagoon.payetonkawa.mail.services;

public interface EmailService {

    boolean sendEmail(String setTo, String code);
}
