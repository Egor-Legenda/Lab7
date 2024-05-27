package org.example.server;
import org.example.common.response.CommandsResponse;
import org.example.server.base.Controller;
import org.example.server.base.DatabaseConfig;
import org.example.server.base.DatabaseConnection;
import org.example.server.data.City;
import org.example.server.data.Climate;
import org.example.server.data.Coordinates;
import org.example.server.data.Human;
import org.example.common.request.CommandsRequest;
import org.example.common.data.*;
import org.example.common.request.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {
    private static final Logger logger = Logger.getGlobal();
    public static Controller controller = new Controller();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        for (String arg: args){
//            Controller.str=Controller.str +arg;
//        }
//
//        if (Controller.str.equals("")){
//            System.out.println("Файл не был введен");
//            System.exit(0);
//        }
//        try {
//            File file =new File(Controller.str);
//            Scanner scan = new Scanner(file);
//        }catch (FileNotFoundException E){
//            Server.logger.info("Файл не найден программа завершена");
//            System.exit(0);
//        }


        //Controller.str ="C:/Users/eshuk/OneDrive/Рабочий стол/лабы/прога/лаба 5.1/City.json";
        Server server =new Server("localhost",6042);
        server.start();


    }


}

//..\..\..\City.json
//C:/Users/eshuk/OneDrive/Рабочий стол/лабы/прога/лаба 5.1/City.json
//execute_script C:/Users/eshuk/OneDrive/Рабочий стол/лабы/прога/лаба 5.1/yrt.txt
//execute_script C:/Users/eshuk/OneDrive/Рабочий стол/лабы/прога/лаба 5.1/tr.txt
//C:\Users\eshuk\OneDrive\Рабочий стол\лабы\прога\лаба 5.1\City.json
//
