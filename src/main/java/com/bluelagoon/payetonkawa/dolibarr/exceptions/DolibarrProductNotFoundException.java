package com.bluelagoon.payetonkawa.dolibarr.exceptions;

public class DolibarrProductNotFoundException extends RuntimeException{
    public DolibarrProductNotFoundException(String message) {
        super(message);
    }
}
