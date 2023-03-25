package com.bluelagoon.payetonkawa.controllers;

import com.bluelagoon.payetonkawa.dolibarr.entities.GenericMessage;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.ProductEntity;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrProductNotFoundException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUnvalidApiKeyException;
import com.bluelagoon.payetonkawa.dolibarr.impl.DolibarrInfraServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DolibarrInfraServiceImpl dolibarrInfraService;

    @Test
    void getProductByIdTest_should_return_status_OK() throws Exception {
        var apiKey = "test";

        var product = new ProductEntity();

        when(dolibarrInfraService.getProductDetailsById(anyString(), anyString()))
                .thenReturn(product);

        mockMvc.perform(get("/products/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, apiKey))
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(product)));
    }

    @Test
    void getProductByIdTest_should_return_status_Bad_Request_when_product_id_is_not_a_number() throws Exception {
        var apiKey = "test";

        var message = new GenericMessage("Le product id fournit n'est pas valide");

        mockMvc.perform(get("/products/product/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, apiKey))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(new Gson().toJson(message)));
    }

    @Test
    void getProductByIdTest_should_return_status_Not_Found_when_product_id_not_exists() throws Exception {
        var apiKey = "test";

        var message = new GenericMessage(null);

        when(dolibarrInfraService.getProductDetailsById(anyString(), anyString()))
                .thenThrow(DolibarrProductNotFoundException.class);

        mockMvc.perform(get("/products/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, apiKey))
                .andExpect(status().isNotFound())
                .andExpect(content().json(new Gson().toJson(message)));
    }

    @Test
    void getProductByIdTest_should_return_status_Bad_Request_when_header_not_found() throws Exception {
        mockMvc.perform(get("/products/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOfTheProductsTest_should_return_status_OK_with_list_of_products() throws Exception {
        var apiKey = "test";

        var product = new ProductEntity();

        var productList = new ArrayList<ProductEntity>();
        productList.add(product);

        when(dolibarrInfraService.getListOfProducts(anyString()))
                .thenReturn(productList);

        mockMvc.perform(get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, apiKey))
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(productList)));
    }

    @Test
    void getAllOfTheProductsTest_should_return_status_Bad_Request() throws Exception {
        var apiKey = "test";

        var message = new GenericMessage(null);

        when(dolibarrInfraService.getListOfProducts(anyString()))
                .thenThrow(DolibarrUnvalidApiKeyException.class);

        mockMvc.perform(get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, apiKey))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(new Gson().toJson(message)));
    }

    @Test
    void getAllOfTheProductsTest_should_return_status_Bad_Request_when_header_not_found() throws Exception {
        mockMvc.perform(get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
