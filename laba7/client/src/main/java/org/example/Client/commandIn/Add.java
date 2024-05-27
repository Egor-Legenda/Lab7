package org.example.Client.commandIn;

public class Add extends Commands implements Command {
    @Override
    public String execution(String arg){
        return man.addManager();
    }

}
