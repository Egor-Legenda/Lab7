package org.example.Client.base;

import org.example.Client.commandIn.*;
import org.example.Client.exceptions.IllegalArgumentException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

public class Controller {

    private String js = "";
    private int i = 1;
    public static boolean authoriz =false;
    public static Integer user_id;
    //private static final Gson Gson = new GsonBuilder().setPrettyPrinting().create();
    CommandControl control =new CommandControl();
    public static String str ="";

    public void createCommand(){
        control.addCommand("help",new Help());
        control.addCommand("exit",new Exit());
        control.addCommand("info",new Info());
        control.addCommand("show",new Show());
        control.addCommand("clear",new Clear());
        control.addCommand("min_by_capital",new MinByCapital());
        control.addCommand("remove_head",new RemoveHead());
        control.addCommand("head",new Head());
        control.addCommand("count_less_than_climate",new CountLessThanClimate());
        control.addCommand("filter_starts_with_name",new FilterStartsWithName());
        control.addCommand("remove_by_id",new RemoveById());
        control.addCommand("execute_script",new ExecuiteScript());
        control.addCommand("add",new Add());
        control.addCommand("add_if_max",new AddIfMax());
        control.addCommand("update",new Update());
        control.addCommand("reg_in",new RegIn());
        control.addCommand("log_in",new Authorization());
        control.addCommand("out",new Out());

    }
    public void run() throws FileNotFoundException {

        if (CommandControl.mode==1) {
            ExecuiteScript execuiteScript =new ExecuiteScript();
            execuiteScript.execution(null);
            return;
        }
        System.out.println("Введите команду:");
        //CollectionManager.cities =(converter.cityFromJson(tip));
        Scanner scanner =new Scanner(System.in);
        //System.out.println("Введите название файла:");
        CollectionManager manager =new CollectionManager();
        String command =null;
        try {
            command = scanner.nextLine().trim();
        }catch (java.util.NoSuchElementException e){
            Exit exit = new Exit();
            exit.execution("exit");
        }
        // Разделяем строку по пробелам
        String[] argument = command.split("\\s+");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(argument));
        String[] words=new String[0];
        String line="";
        int danger=0;
        int in=0;
        try{
            for(String str: arrayList){
                if ((str.equals(" ") || str.equals(""))&&(danger==0)){
                    if (str.equals(" ")){
                        arrayList.remove(in);
                    }
                    if (str.equals("")){
                        arrayList.remove(in);
                    }
                }else {
                    danger+=1;
                }
                in+=1;

            }
            words = arrayList.toArray(new String[0]);
            if (words.length > 1) {
                // Получаем первое слово
                command = words[0];
                String firstWord=words[0];

                // Удаляем пробелы перед следующими словами
                StringBuilder result = new StringBuilder("");
                for (int i = 1; i < words.length; i++) {
                    result.append(words[i]);
                    line = line + " " + words[i];
                }

                // Выводим результат
                line=line.trim();
            }else {
                command=words[0];
            }
        }catch (ConcurrentModificationException У){

        }

                //String command = scanner.nextLine();
                //WorkFile file = new WorkFile();
        command =command.toLowerCase();
        CollectionManager.command_1=command;

        String str;
        if (authoriz ==true) {

            switch (command) {
                case "info", "help", "exit", "show", "clear", "head", "remove_head", "min_by_capital":
                    if (words.length == 1) {
                        str = (control.getCommand(command).execution(command));
                    } else {
                        System.out.println("Данная команда вводится без аргумента");
                        run();
                    }

                    break;
                case "count_less_than_climate", "filter_starts_with_name", "remove_by_id", "update":
                    if (words.length == 2) {
                        str = (control.getCommand(command).execution(line));
                    } else if (words.length == 1) {
                        System.out.println("Данная команда вводится с аргументом");
                        run();
                    } else {
                        System.out.println("Данная команда вводится с одним аргументом");
                        run();
                    }
                    break;
                case "add_if_max", "add":

                    if (words.length == 1) {
                        str = (control.getCommand(command).execution(command));
                    } else {
                        System.out.println("Данная команда вводится без аргумента");
                        run();
                    }

                    break;

                case "execute_script":
                    str = (control.getCommand(command).execution(line));
                    break;
                case "out":
                    if (words.length == 1) {
                        System.out.println((control.getCommand(command).execution(command)));
                    } else {
                        System.out.println("Данная команда вводится без аргумента");
                        run();
                    }
                    break;
                default:
                    System.out.println(new IllegalArgumentException("Неизвестная команда: " + command));
                    run();
                    break;
            }
        } else {
            switch (command) {
                case "log_in", "reg_in":
                    if (words.length == 1) {
                        str = (control.getCommand(command).execution(command));
                    } else {
                        System.out.println("Данная команда вводится без аргумента");
                        run();
                    }

                    break;
                default:
                    System.out.println(new IllegalArgumentException("Неизвестная команда: " + command+". Авторизируйтесь используя команду log_in или зарегистрируйтесь, используя reg_in"));
                    run();
                    break;
            }
        }
    }
}
