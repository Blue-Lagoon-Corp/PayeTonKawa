package com.bluelagoon.payetonkawa.dolibarr.impl;

import com.bluelagoon.payetonkawa.dolibarr.entities.input.LoginEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.ProductEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.SuccessEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.TokenEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.UserInfoEntity;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrInternalErrorServorException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrProductNotFoundException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUnvalidApiKeyException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class DolibarrInfraServiceImplTest {

    @Mock
    private WebClient webClientMock;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @Mock
    private WebClient.RequestBodySpec requestBodySpecMock;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @InjectMocks
    private DolibarrInfraServiceImpl dolibarrInfraService;

    @Test
    void loginTest_should_return_a_valid_SuccessEntity() {
        var loginEntity = new LoginEntity();
        loginEntity.setLogin("okilic");
        loginEntity.setPassword("test");

        var tokenEntity = new TokenEntity();
        tokenEntity.setCode(200);
        tokenEntity.setToken("test");

        var successEntity = new SuccessEntity();
        successEntity.setSuccess(tokenEntity);

        lenient().when(webClientMock.post())
                .thenReturn(requestBodyUriSpecMock);

        lenient().when(requestBodyUriSpecMock.uri(anyString()))
                .thenReturn(requestBodySpecMock);

        lenient().when(requestBodySpecMock.bodyValue(any()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<SuccessEntity>>notNull()))
                .thenReturn(Mono.just(successEntity));


        var expectedTokenEntity = new TokenEntity();
        expectedTokenEntity.setToken("test");
        expectedTokenEntity.setCode(200);

        var expectedSuccessEntity = new SuccessEntity();
        expectedSuccessEntity.setSuccess(expectedTokenEntity);

        var resultSuccessEntity = dolibarrInfraService.login(loginEntity);

        assertEquals(expectedSuccessEntity, resultSuccessEntity);
    }

    @Test
    void loginTest_should_return_a_DolibarrUserNotFoundException() {

        var loginEntity = new LoginEntity();
        loginEntity.setLogin("okilic");
        loginEntity.setPassword("oto");

        var dolibarrUserNotFoundException = new DolibarrUserNotFoundException("Login ou mot de passe incorrect");

        lenient().when(webClientMock.post())
                .thenReturn(requestBodyUriSpecMock);

        lenient().when(requestBodyUriSpecMock.uri(anyString()))
                .thenReturn(requestBodySpecMock);

        lenient().when(requestBodySpecMock.bodyValue(any()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                        .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                        .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<SuccessEntity>>notNull()))
                .thenReturn(Mono.error(dolibarrUserNotFoundException));

        assertThrows(DolibarrUserNotFoundException.class, ()-> dolibarrInfraService.login(loginEntity));
    }

    @Test
    void loginTest_should_return_a_DolibarrInternalErrorServerException() {

        var loginEntity = new LoginEntity();
        loginEntity.setLogin("okilic");
        loginEntity.setPassword("oto");

        var dolibarrInternalErrorServorException = new DolibarrInternalErrorServorException("internal error");

        lenient().when(webClientMock.post())
                .thenReturn(requestBodyUriSpecMock);

        lenient().when(requestBodyUriSpecMock.uri(anyString()))
                .thenReturn(requestBodySpecMock);

        lenient().when(requestBodySpecMock.bodyValue(any()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<SuccessEntity>>notNull()))
                .thenReturn(Mono.error(dolibarrInternalErrorServorException));

        assertThrows(DolibarrInternalErrorServorException.class, ()-> dolibarrInfraService.login(loginEntity));
    }

    @Test
    void getUserEmailByLogin_should_return_a_valid_userEmail() {
        var login = "test";
        var apiKey = "apikey";

        var userInfoEntity = new UserInfoEntity();
        userInfoEntity.setEmail("test@gmail.com");

        lenient().when(webClientMock.get())
                .thenReturn(requestHeadersUriSpecMock);

        lenient().when(requestHeadersUriSpecMock.uri(anyString(), anyString(), anyString()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<UserInfoEntity>>notNull()))
                        .thenReturn(Mono.just(userInfoEntity));

        assertNotNull(dolibarrInfraService.getUserEmailByLogin(login, apiKey));
    }

    @Test
    void getProductDetailsByIdTest_should_return_a_not_null_product() {
        var productId = "1";
        var apiKey = "apikey";

        var product = new ProductEntity();

        lenient().when(webClientMock.get())
                .thenReturn(requestHeadersUriSpecMock);

        lenient().when(requestHeadersUriSpecMock.uri(anyString(), anyString(), anyString()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                        .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<ProductEntity>>notNull()))
                .thenReturn(Mono.just(product));

        assertNotNull(dolibarrInfraService.getProductDetailsById(productId, apiKey));
    }

    @Test
    void getProductDetailsByIdTest_should_throw_a_ProductNotFoundException() {
        var productId = "1";
        var apiKey = "apikey";

        var dolibarrProductNotFoundException = new DolibarrProductNotFoundException("product not found");

        lenient().when(webClientMock.get())
                .thenReturn(requestHeadersUriSpecMock);

        lenient().when(requestHeadersUriSpecMock.uri(anyString(), anyString(), anyString()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<ProductEntity>>notNull()))
                .thenReturn(Mono.error(dolibarrProductNotFoundException));

        assertThrows(DolibarrProductNotFoundException.class, ()-> dolibarrInfraService.getProductDetailsById(productId, apiKey));
    }

    @Test
    void getListOfProductsTest_should_return_not_null() {
        var apiKey = "apikey";

        var productEntity = new ProductEntity();

        var productList = new ArrayList<ProductEntity>();
        productList.add(productEntity);

        lenient().when(webClientMock.get())
                .thenReturn(requestHeadersUriSpecMock);

        lenient().when(requestHeadersUriSpecMock.uri(anyString(), anyString()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                        .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.bodyToFlux(ArgumentMatchers.<Class<List<ProductEntity>>>notNull()))
                .thenReturn(Flux.just(productList));

        assertNotNull(dolibarrInfraService.getListOfProducts(apiKey));
    }

    @Test
    void getListOfProductsTest_should_return_4XX() {
        var apiKey = "apikey";

        var dolibarrUnvalidApiKeyNotFoundException = new DolibarrUnvalidApiKeyException("API KEY invalide");

        lenient().when(webClientMock.get())
                .thenReturn(requestHeadersUriSpecMock);

        lenient().when(requestHeadersUriSpecMock.uri(anyString(), anyString()))
                .thenReturn(requestHeadersSpecMock);

        lenient().when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);

        lenient().when(responseSpecMock.onStatus(any(), any()))
                .thenThrow(dolibarrUnvalidApiKeyNotFoundException);

        assertThrows(DolibarrUnvalidApiKeyException.class, () -> dolibarrInfraService.getListOfProducts(apiKey));
    }


}
