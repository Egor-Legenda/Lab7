package org.example.Client.commandIn;

public class Out extends Commands implements Command{

    @Override
    public String execution(String arg) {
        return man.outManager();
    }
}
