package com.bluelagoon.payetonkawa.controllers;

import com.bluelagoon.payetonkawa.mail.interfaces.EmailInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private EmailInterface emailInterface;

    @GetMapping("/toto")
    public String test(){
        emailInterface.sendEmail("oguzhan.kilic@epsi.fr");
        return "mail sended";
    }
}
