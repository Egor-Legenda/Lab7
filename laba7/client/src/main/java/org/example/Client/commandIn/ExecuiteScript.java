package org.example.Client.commandIn;

public class ExecuiteScript extends Commands implements Command {
    public static String path;

    @Override
    public String execution(String arg){
        return man.execuiteScript(arg);
    }
}
