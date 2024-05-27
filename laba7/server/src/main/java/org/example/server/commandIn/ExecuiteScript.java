package org.example.server.commandIn;
import org.example.server.data.City;
public class ExecuiteScript extends Commands implements Command{
    public static String path;

    @Override
    public String execution(String arg,City city, String password, String user,Integer id){
        //return man.execuiteScript(arg);
        return null;
    }
}
