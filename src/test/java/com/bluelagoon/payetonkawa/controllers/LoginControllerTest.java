package com.bluelagoon.payetonkawa.controllers;

import com.bluelagoon.payetonkawa.dolibarr.entities.GenericMessage;
import com.bluelagoon.payetonkawa.dolibarr.entities.input.LoginEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.SuccessEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.TokenEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.UserInfoEntity;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrInternalErrorServorException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUserNotFoundException;
import com.bluelagoon.payetonkawa.dolibarr.impl.DolibarrInfraServiceImpl;
import com.bluelagoon.payetonkawa.mail.impl.EmailServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DolibarrInfraServiceImpl dolibarrInfraService;

    @MockBean
    private EmailServiceImpl emailService;

    @Test
    void loginTest_should_return_status_OK() throws Exception {
        var tokenEntity = new TokenEntity();
        tokenEntity.setToken("test");
        tokenEntity.setCode(200);

        var expectedSuccessEntity = new SuccessEntity();
        expectedSuccessEntity.setSuccess(tokenEntity);

        var userInfoEntity = new UserInfoEntity();
        userInfoEntity.setEmail("test@gmail.com");

        var login = new LoginEntity();
        login.setLogin("test");
        login.setPassword("test");

        var expectedResponseBody = new GenericMessage("Connexion réussi, un mail vous a été envoyé");

        when(dolibarrInfraService.login(any())).thenReturn(expectedSuccessEntity);

        when(dolibarrInfraService.getUserEmailByLogin(any(), anyString())).thenReturn(userInfoEntity);

        when(emailService.sendEmail(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(login)))
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(expectedResponseBody)));
    }

    @Test
    void loginTest_should_return_status_Internal_Error_Servor_when_sending_mail() throws Exception {
        var tokenEntity = new TokenEntity();
        tokenEntity.setToken("test");
        tokenEntity.setCode(200);

        var expectedSuccessEntity = new SuccessEntity();
        expectedSuccessEntity.setSuccess(tokenEntity);

        var userInfoEntity = new UserInfoEntity();
        userInfoEntity.setEmail("test@gmail.com");

        var login = new LoginEntity();
        login.setLogin("test");
        login.setPassword("test");

        var expectedResponseBody = new GenericMessage("Erreur interne au serveur lors de l'envoi de mail");

        when(dolibarrInfraService.login(any())).thenReturn(expectedSuccessEntity);

        when(dolibarrInfraService.getUserEmailByLogin(any(), anyString())).thenReturn(userInfoEntity);

        when(emailService.sendEmail(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(login)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(new Gson().toJson(expectedResponseBody)));
    }

    @Test
    void loginTest_should_return_status_Internal_Error_Servor_when_a_dolibarr_crash_happens() throws Exception {
        var login = new LoginEntity();
        login.setLogin("test");
        login.setPassword("test");

        var expectedResponseBody = new GenericMessage(null);

        when(dolibarrInfraService.login(any())).thenThrow(DolibarrInternalErrorServorException.class);

        mockMvc.perform(post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(login)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(new Gson().toJson(expectedResponseBody)));
    }

    @Test
    void loginTest_should_return_status_Forbidden_when_user_not_found() throws Exception {
        var login = new LoginEntity();
        login.setLogin("test");
        login.setPassword("test");

        var expectedResponseBody = new GenericMessage(null);

        when(dolibarrInfraService.login(any())).thenThrow(DolibarrUserNotFoundException.class);

        mockMvc.perform(post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(login)))
                .andExpect(status().isForbidden())
                .andExpect(content().json(new Gson().toJson(expectedResponseBody)));
    }
}
