package org.example.Client.commandIn;

public class FilterStartsWithName extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.filterStartsWithNameManager(arg);
    }

}
