package com.bluelagoon.payetonkawa.controllers;

import com.bluelagoon.payetonkawa.dolibarr.entities.GenericMessage;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.ProductEntity;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrProductNotFoundException;
import com.bluelagoon.payetonkawa.dolibarr.exceptions.DolibarrUnvalidApiKeyException;
import com.bluelagoon.payetonkawa.dolibarr.services.DolibarrInfraService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private final DolibarrInfraService dolibarrInfraService;

    public ProductsController(DolibarrInfraService dolibarrInfraService) {
        this.dolibarrInfraService = dolibarrInfraService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getProductById(@RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey,
                                                 @PathVariable String productId){
        if (!pattern.matcher(productId).matches()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericMessage("Le product id fournit n'est pas valide"));
        }
        ProductEntity product;
        try {
            product = dolibarrInfraService.getProductDetailsById(productId, apiKey);
        }catch (DolibarrProductNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericMessage(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOfTheProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String apiKey){
        List<ProductEntity> productList;
        try{
            productList = dolibarrInfraService.getListOfProducts(apiKey);
        }catch (DolibarrUnvalidApiKeyException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericMessage(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

}
