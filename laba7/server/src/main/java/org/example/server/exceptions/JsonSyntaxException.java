package org.example.server.exceptions;

public class JsonSyntaxException extends RuntimeException{
    public JsonSyntaxException(String massange){
        super(massange);
    }
}
