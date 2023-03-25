package com.bluelagoon.payetonkawa.dolibarr.exceptions;

public class DolibarrUnvalidApiKeyException extends RuntimeException{
    public DolibarrUnvalidApiKeyException(String message) {
        super(message);
    }
}
