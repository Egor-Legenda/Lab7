package org.example.server.base;

import org.example.server.commandIn.Commands;

import java.util.*;

public class CommandControl {
    public static int work=0;
    public String path ="";
    public static int count =0;
    public static int mode =0;
    public static String inputData="";
    public static int length;
    private Scanner scanner =new Scanner(System.in);
    public static int is=-1;
    private static Map<String, Commands> comands = new HashMap<>();
    public void addCommand(String name,Commands command){
        comands.put(name,command);
    }
    public Commands getCommand(String name){
        return comands.get(name);
    }


}
