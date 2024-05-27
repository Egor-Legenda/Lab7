package org.example.server.exceptions;

public class ConcurrentModificationException extends  RuntimeException{
    public ConcurrentModificationException(String massange){
        super(massange);
    }
}
