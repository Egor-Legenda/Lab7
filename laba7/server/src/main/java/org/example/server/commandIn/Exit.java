package org.example.server.commandIn;

import org.example.server.data.City;

public class Exit extends Commands implements Command {
    @Override
    public String execution(String arg, City city, String password, String user,Integer id){
        return man.exitManager();

    }

}
