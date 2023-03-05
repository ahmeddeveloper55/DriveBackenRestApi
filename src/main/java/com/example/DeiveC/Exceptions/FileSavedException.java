package com.example.DeiveC.Exceptions;

public class FileSavedException extends RuntimeException{
    public FileSavedException(String message) {
        super(message);
    }

    public FileSavedException(String message, Throwable cause) {
        super(message, cause);
    }
}
