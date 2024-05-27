package org.example.server.base;

import org.example.server.Server;

import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileOutputStream;
public class WorkFile {
    private String json="";
    private ReadLine readerline =new ReadLine();
    CommandControl control = new CommandControl();
    private int count=0;
    public String reader(String name){
        try {
            count=0;
            File file =new File(name);
            Scanner scan = new Scanner(file);//
            while (scan.hasNextLine()) {
                count+=1;
                json = json + scan.nextLine()+"\n";
            }
            CommandControl.inputData=name;
            CommandControl.length =count;
            Server.logger.info("Файл найден");
            scan.close();
        }catch (java.io.FileNotFoundException E){
            Server.logger.info("Файл не найден, коллекция будет пустой ");
            //reader(control.reader());
        }
        return json;
    }
    public void writer(String path,String json) { //Запись в файл
        File file = new File(path);
        try (OutputStream out = new FileOutputStream(file)) {
            byte[] js = json.getBytes();// Перевод в байт код
            out.write(js);
            out.close();//Необязательно
            Server.logger.info("Загружено");
        } catch (java.io.FileNotFoundException E) { // Нет файла
            Server.logger.info("Файл не найден ");
            writer(readerline.read(), json);

        } catch (IOException e) {// Ошибка доступа к файлу и ее обработка
            Server.logger.info("Файл недоступен");
            writer(readerline.read(), json);
        }
    }
    public ArrayList<String> readerArray(String name){////
        String[] js;
        ArrayList<String> list=null;
        try {
            File file = new File(name);
            Scanner scan = new Scanner(file);//
            while (scan.hasNextLine()) {
                list.add(scan.nextLine());
            }
            scan.close();
            return list;
        }catch (java.io.FileNotFoundException E){
            Server.logger.info("Файл не найден");
            return readerArray(readerline.read());
        }

    }


}
