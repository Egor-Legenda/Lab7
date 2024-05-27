package org.example.server.commandIn;

import org.example.server.data.City;

public class Save extends Commands implements Command{
    @Override
    public String execution(String arg, City city, String password, String user,Integer id){
        //return man.saveManager();
        return  null;

    }
}
