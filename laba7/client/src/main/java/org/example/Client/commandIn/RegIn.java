package org.example.Client.commandIn;

public class RegIn extends Commands implements Command{


    @Override
    public String execution(String arg) {
        return man.regInManager();
    }
}
