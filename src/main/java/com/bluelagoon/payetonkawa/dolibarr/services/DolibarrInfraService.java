package com.bluelagoon.payetonkawa.dolibarr.services;

import com.bluelagoon.payetonkawa.dolibarr.entities.input.LoginEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.ProductEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.SuccessEntity;
import com.bluelagoon.payetonkawa.dolibarr.entities.output.UserInfoEntity;

import java.util.List;

public interface DolibarrInfraService {

    /**
     * This method will instantiate the connection of the user by sending request to the ERP.
     * @param login Object of type LoginEntity which require login and password
     * @return a SuccessEntity which contains the api key and status code of the request
     */
    SuccessEntity login(LoginEntity login);

    /**
     * This method will get the email of the user by his login
     * @param userLogin login of the user
     * @param apiKey apiKey of the user
     * @return an Object of type UserInfoEntity which contains the email of the user
     */
    UserInfoEntity getUserEmailByLogin(String userLogin, String apiKey);

    /**
     * This method will get all the product details from the product id
     * @param productId product id of the searched product
     * @param apiKey apikey to perform the search
     * @return a ProductEntity object contains all the product details
     */
    ProductEntity getProductDetailsById(String productId, String apiKey);

    /**
     * This method will get all the products
     * @param apiKey apikey to perform the search
     * @return a list of all the products
     */
    List<ProductEntity> getListOfProducts(String apiKey);
}
