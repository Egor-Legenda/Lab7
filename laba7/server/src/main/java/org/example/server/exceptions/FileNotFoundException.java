package org.example.server.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String massange){
        super(massange);
    }
}
