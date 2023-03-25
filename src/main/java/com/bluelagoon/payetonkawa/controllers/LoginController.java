package com.bluelagoon.payetonkawa.controllers;

import com.bluelagoon.payetonkawa.dolibarr.entities.GenericMessage;
import com.bluelagoon.payetonkawa.dolibarr.entities.input.LoginEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.SuccessEntity;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrInternalErrorServorException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUserNotFoundException;
import com.bluelagoon.payetonkawa.dolibarr.services.DolibarrInfraService;
import com.bluelagoon.payetonkawa.mail.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class LoginController {

    private final EmailService emailService;

    private final DolibarrInfraService dolibarrInfraService;

    public LoginController(EmailService emailService, DolibarrInfraService dolibarrInfraService) {
        this.emailService = emailService;
        this.dolibarrInfraService = dolibarrInfraService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericMessage> login(@RequestBody LoginEntity loginEntity){
        SuccessEntity successEntity;
        try {
            successEntity = dolibarrInfraService.login(loginEntity);
        } catch (DolibarrInternalErrorServorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericMessage(e.getMessage()));
        } catch (DolibarrUserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericMessage(e.getMessage()));
        }

        if (successEntity.getSuccess().getCode() == HttpStatus.OK.value()){
            var userEmail = dolibarrInfraService.getUserEmailByLogin(loginEntity.getLogin(),
                    successEntity.getSuccess().getToken());

            var isMailSended = emailService.sendEmail(userEmail.getEmail(), successEntity.getSuccess().getToken());

            if (!isMailSended){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericMessage("Erreur interne au serveur lors de l'envoi de mail"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new GenericMessage("Connexion réussi, un mail vous a été envoyé"));

        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericMessage("Erreur interne lors de la récupération de l'email"));
    }
}
