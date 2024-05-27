package org.example.Client.commandIn;

public class Clear extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.clearManager();
    }

}
