package org.example.Client.commandIn;

public class Exit extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.exitManager();
    }

}
