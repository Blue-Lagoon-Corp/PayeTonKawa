package com.bluelagoon.payetonkawa.dolibarr.impl;

import com.bluelagoon.payetonkawa.dolibarr.entities.input.LoginEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.ProductEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.SuccessEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.UserInfoEntity;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrInternalErrorServorException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrProductNotFoundException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUnvalidApiKeyException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUserNotFoundException;
import com.bluelagoon.payetonkawa.dolibarr.services.DolibarrInfraService;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DolibarrInfraServiceImpl implements DolibarrInfraService {

    private final WebClient webClient;

    public DolibarrInfraServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public SuccessEntity login(LoginEntity login) {
        return webClient.post()
                .uri("/login")
                .bodyValue(login)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse ->
                                Mono.error(new DolibarrInternalErrorServorException("Erreur interne Serveur Dolibarr"))
                )
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse ->
                                Mono.error(new DolibarrUserNotFoundException("Login ou mot de passe incorrect"))
                )
                .bodyToMono(SuccessEntity.class)
                .log("Request to Dolibarr server to login ...")
                .block();
    }

    @Override
    public UserInfoEntity getUserEmailByLogin(String userLogin, String apiKey) {
        return webClient.get()
                .uri("/users/login/{userLogin}?DOLAPIKEY={apiKey}", userLogin, apiKey)
                .retrieve()
                .bodyToMono(UserInfoEntity.class)
                .log("Request to Dolibarr server to get user info for login : "+userLogin)
                .block();
    }

    @Override
    public ProductEntity getProductDetailsById(String productId, String apiKey) {
        return webClient.get()
                .uri("/products/{productId}?DOLAPIKEY={apiKey}", productId, apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse ->
                                Mono.error(new DolibarrProductNotFoundException("Produit introuvable")))
                .bodyToMono(ProductEntity.class)
                .log("Request to Dolibarr server to get product details with id: "+productId)
                .block();
    }

    @Override
    public List<ProductEntity> getListOfProducts(String apiKey) {
        return webClient.get()
                .uri("/products?DOLAPIKEY={apiKey}", apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse ->
                                Mono.error(new DolibarrUnvalidApiKeyException("API KEY invalide")))
                .bodyToFlux(ProductEntity.class)
                .log("Request to Dolibarr server to get all products")
                .collectList()
                .block();
    }
}
