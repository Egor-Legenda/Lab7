package org.example.Client.commandIn;

public class RemoveById extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.removeByIdManager(arg);
    }
}
