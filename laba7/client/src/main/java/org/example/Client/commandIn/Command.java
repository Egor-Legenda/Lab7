package org.example.Client.commandIn;

import org.example.Client.base.CollectionManager;
import org.example.Client.base.CommandControl;

public interface Command {
    CollectionManager man =new CollectionManager(); //Создан для работы с командами
    CommandControl control =new CommandControl();
    String execution(String arg);
}
