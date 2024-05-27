package org.example.server.exceptions;

public class NoSuchElementException extends RuntimeException{
    public NoSuchElementException(String massange){//При вводе 0
        super(massange);
    }
}
