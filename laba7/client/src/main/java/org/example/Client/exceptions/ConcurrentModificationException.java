package org.example.Client.exceptions;

public class ConcurrentModificationException extends  RuntimeException{
    public ConcurrentModificationException(String massange){
        super(massange);
    }
}
