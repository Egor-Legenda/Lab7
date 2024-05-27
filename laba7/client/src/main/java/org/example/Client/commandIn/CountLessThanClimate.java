package org.example.Client.commandIn;

public class CountLessThanClimate extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.countLessThanClimateManager(arg);
    }


}
