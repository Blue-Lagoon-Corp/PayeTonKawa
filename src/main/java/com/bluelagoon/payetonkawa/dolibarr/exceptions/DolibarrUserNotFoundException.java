package com.bluelagoon.payetonkawa.dolibarr.exceptions;

public class DolibarrUserNotFoundException extends RuntimeException{
    public DolibarrUserNotFoundException(String message) {
        super(message);
    }
}
