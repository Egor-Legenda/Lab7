package org.example.Client.commandIn;

public class Authorization extends Commands implements Command{

    @Override
    public String execution(String arg) {
        return man.authorizationManager();
    }
}
