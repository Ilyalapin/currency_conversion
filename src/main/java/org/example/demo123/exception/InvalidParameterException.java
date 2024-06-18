package org.example.demo123.exception;

public class InvalidParameterException extends RuntimeException{
    public InvalidParameterException(String message) {
        super(message);
    }
}
