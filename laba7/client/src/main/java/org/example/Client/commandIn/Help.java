package org.example.Client.commandIn;

public class Help extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.helpManager();
    }
}
