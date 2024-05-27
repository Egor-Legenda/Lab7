package org.example.server.commandIn;

import org.example.server.base.CollectionManager;
import org.example.server.base.CommandControl;
import org.example.server.data.City;

public interface Command {
    CollectionManager man =new CollectionManager(); //Создан для работы с командами
    CommandControl control =new CommandControl();
    String execution(String arg, City city, String password, String user,Integer id);
}
