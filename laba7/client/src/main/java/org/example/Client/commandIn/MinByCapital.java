package org.example.Client.commandIn;

public class MinByCapital extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.minByCapitalManager();
    }

}
