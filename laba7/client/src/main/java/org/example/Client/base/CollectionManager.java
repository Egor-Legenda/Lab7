package org.example.Client.base;

import org.example.common.request.CommandsRequest;


import org.example.common.data.*;
import org.example.Client.exceptions.IllegalArgumentException;
import org.example.Client.exceptions.InputMismatchException;
import org.example.Client.exceptions.NullPointerException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
/*import Egor.example.exceptions.NumberFormatException;*/

public class CollectionManager {
    private LocalDateTime time = LocalDateTime.now();
    private static Integer notId = 0;
    //public static LinkedList<City> cities = new LinkedList<>();
    public static String command_1;
    private WorkFile workFile = new WorkFile();

    private boolean flag = true;
    private CommandControl control = new CommandControl();
    public static ArrayList<String> filesName = new ArrayList<String>();
    public static List<FilePathLengthCount> listPath = new ArrayList<>();
    Random random = new Random();

    private City city_1 = null;
    public static int cycle = 0;
    private int anInt = 1;
    private static long id = 1;
    private String[] path = new String[100];
    public static CommandsRequest request;



    public String regInManager(){
        int count = 0;
        String name;
        String password;
        do {
            if (count == 0) {
                System.out.println("Введите имя пользователя, под которым хотите зарегистрироваться:");
                count += 1;
            } else {
                System.out.println("Имя не должна быть пустым или равным null. Введите повторно имя пользователя:");
            }
            name = control.reader().trim();
            if (name != null && name != "") {
                flag = true;
            }
        } while (flag == false);
        count=0;
        flag=false;
        do {
            if (count == 0) {
                System.out.println("Введите пароль для пользователя:");
                count += 1;
            } else {
                System.out.println("Пароль не должна быть пустым или равным null. Введите повторно пароль:");
            }
            password = control.reader().trim();
            if (password != null && password != "") {
                flag = true;
            }
        } while (flag == false);
        request =new CommandsRequest("reg_in",name,password);
        return "Пользователь добавлен";
    }
    public String authorizationManager(){
        int count = 0;
        String name;
        String password;
        do {
            if (count == 0) {
                System.out.println("Введите имя пользователя:");
                count += 1;
            } else {
                System.out.println("Имя не должна быть пустым или равным null. Введите повторно имя пользователя:");
            }
            name = control.reader().trim();
            if (name != null && name != "") {
                flag = true;
            }
        } while (flag == false);
        count=0;
        flag=false;
        do {
            if (count == 0) {
                System.out.println("Введите пароль для пользователя:");
                count += 1;
            } else {
                System.out.println("Пароль не должна быть пустым или равным null. Введите повторно пароль:");
            }
            password = control.reader().trim();
            if (password != null && password != "") {
                flag = true;
            }
        } while (flag == false);
        request =new CommandsRequest("log_in",name,password);
        return "Пользователь в поиске";
    }
    public String outManager(){

        request = new CommandsRequest("skip");
        Controller.authoriz =false;
        return "Пользователь разлогинился";
    }
    public String helpManager() {
        //return "help: вывести справку по доступным командам\n" + "info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" + "show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" + "add {element}: добавить новый элемент в коллекцию\n" + "update id {element}: обновить значение элемента коллекции, id которого равен заданному\n" + "remove_by_id id: удалить элемент из коллекции по его id\n" + "clear: очистить коллекцию\n" + "save: сохранить коллекцию в файл\n" + "execute_script file_name: считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" + "exit: завершить программу (без сохранения в файл)\n" + "head: вывести первый элемент коллекции\n" + "remove_head: вывести первый элемент коллекции и удалить его\n" + "add_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" + "min_by_capital: вывести любой объект из коллекции, значение поля capital которого является минимальным\n" + "count_less_than_climate climate: вывести количество элементов, значение поля climate которых меньше заданного\n" + "filter_starts_with_name name: вывести элементы, значение поля name которых начинается с заданной подстроки";
        request = new CommandsRequest("help");
        return null;
    }

    public String exitManager() {
        System.exit(0);
        return "Выход";
    }

    public String infoManager() {


        request = new CommandsRequest("info");
        return null;
    }


    public String showManager() {

        request = new CommandsRequest("show");
        return null;

    }

    public String clearManager() {

        request = new CommandsRequest("clear",Controller.user_id);
        return null;
    }

    public String headManager() {

        request = new CommandsRequest("head");
        return null;
    }

    public String removeHeadManager() {

        request = new CommandsRequest("remove_head",Controller.user_id);
        return null;

    }



    public String minByCapitalManager() {


        request = new CommandsRequest("min_by_capital");

        return null;
    }

    public String filterStartsWithNameManager(String search) {


        request = new CommandsRequest("filter_starts_with_name", search);
        return null;

    }


    public String countLessThanClimateManager(String search1) {

        int hops = 0;
        Climate inputEnum = null;
        String search =search1.toUpperCase();
        for (Climate enumValue : EnumSet.allOf(Climate.class)) {
            if (enumValue.name().equals(search)) {

                inputEnum = Climate.valueOf(search);
                break;
            }
        }
        if (inputEnum == null) {
            request = new CommandsRequest("skip", search);
            System.out.println("Такого enum не найдено. Enum могли быть: OCEANIC, TUNDRA, DESERT");
            return "Такого enum не найдено. Enum могли быть: OCEANIC, TUNDRA, DESERT";
        }


        request = new CommandsRequest("count_less_than_climate", String.valueOf(inputEnum));

        return null;

//        String result = "";
//        for (City city : cities) {
//            if (city.getClimate() == inputEnum) {
//                continue;
//            } else {
//                result += "\n"+city.toString();
//            }
//
//        }
//        CommandControl.work=0;
//        if (result==""){
//            return "У всех городов климат"+inputEnum.name();
//        }else {
//            return "Вот города у которых климат неравен " + inputEnum.name() + ": " + result;
//        }
    }

    public String removeByIdManager(String str) {
        int index = 0;
        String res = "";
        try {

            index = Integer.parseInt(str);
            if (index <= 0) {
                request = new CommandsRequest("skip");
                return "Такого индекса не существует";
            }
        } catch (NumberFormatException e) {
            request = new CommandsRequest("skip");
            return "Не тот формат данных";
        } catch (NullPointerException e) {
            request = new CommandsRequest("skip");

            return "Нельзя ввести пумстую строку";
        }
        request = new CommandsRequest("remove_by_id", str, Controller.user_id);

//        for (City city:cities){
//            if(city.getId()==index){
//                res+=city.toString();
//                cities.remove(city);
//            }
//        }
//        CommandControl.work=0;
//        if (res==""){
//            return "Городов с данным id не было найдено";
//        }else {
//            return "Были удалены: " + res;
//        }
        return null;
    }

    public String addManager() {
        //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
        String name = ""; //Поле не может быть null, Строка не может быть пустой
        Coordinates coordinates = null; //Поле не может быть null
        LocalDateTime creationDate = LocalDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
        float area = 0; //Значение поля должно быть больше 0, Поле не может быть null
        long population = 0; //Значение поля должно быть больше 0
        double metersAboveSeaLevel = 0;
        boolean capital = false; //Поле не может быть null
        double populationDensity = 0; //Значение поля должно быть больше 0
        Climate climate = null; //Поле не может быть null
        Human governor = new Human(LocalDateTime.now()); //Поле не может быть null
        float x = 0; //Значение поля должно быть больше -979
        Double y = null; //Поле не может быть null
        int count = 0;
//        if (notId==0) {
//
//
//
//            try {
//
//                System.out.println("Город будет добавлен под таким id ");
//                id=1;
//                for (City city : cities) {
//                    if (city.getId() >= id) {
//
//                        id = city.getId();
//                    }
//                }
//                id+=1;
//                System.out.println(id);
//
//            } catch (NumberFormatException | NullPointerException | java.util.InputMismatchException e) {
//
//            }
//
//        }

        count = 0;
        boolean flag = false;
        do {
            if (count == 0) {
                System.out.println("Введите название города:");
                count += 1;
            } else {
                System.out.println("Строка не должна быть пустой или равной null. Введите повторно название города:");
            }
            name = control.reader().trim();
            if (name != null && name != "") {
                flag = true;
            }
        } while (flag == false);

        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите координаты по x города(больше -979):");

                } else {
                    System.out.println("Некорректный ввод повторите попытку число должно быть больше -979. Повторите ввод:");
                }
                String x1 = (control.reader().trim());
                flag = examinationFloat(x1);
                x = Float.parseFloat(x1);

                if (x > -979 && flag == true) {
                    flag = true;
                }
            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                flag = false;
            }
        } while (flag == false);

        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите координаты по y города:");

                } else {
                    System.out.println("Некорректный ввод повторите попытку число должно быть != null. Повторите ввод:");
                }

                y = Double.valueOf(control.reader().trim());
                flag = true;
            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                flag = false;
            }
        } while (flag == false);

        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите площадь города:");

                } else {
                    System.out.println("Площадь города должна быть больше 0. Повторите ввод:");
                }
                String area1 = control.reader().trim();
                flag = examinationFloat(area1);
                area = Float.valueOf(area1);
                if (area > 0 && flag == true) {
                    flag = true;
                }
            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                flag = false;
            }
        } while (flag == false);


        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите популяцию города:");

                } else {
                    System.out.println("Популяция города должна быть больше 0. Повторите ввод:");
                }
                String pop1 = control.reader().trim();
                flag = examinationLong(pop1);
                population = Long.valueOf(pop1);
                if (population > 0 && flag == true) {
                    flag = true;
                }
            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                flag = false;
            }
        } while (flag == false);

        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите уровень над морем города:");

                } else {
                    System.out.println("Некоректный ввод уровня он должен быть числом:");
                }
                metersAboveSeaLevel = Double.valueOf(control.reader().trim());
                flag = true;
            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                flag = false;
            }
        } while (flag == false);


        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите капитал(true или false):");

                } else {
                    System.out.println("Капитал может быть только true или false:");
                }
                String str = control.reader().toLowerCase().trim();

                if (str.equalsIgnoreCase("true")) {
                    capital = true;
                    flag = true;
                }
                if (str.equalsIgnoreCase("false")) {
                    capital = false;
                    flag = true;
                }
            } catch (IllegalArgumentException e) {
                flag = false;
            }
        } while (flag == false);

        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите популяцию города на кв.м.:");

                } else {
                    System.out.println("Популяция города на кв.м. должна быть больше 0. Повторите ввод:");
                }
                String pop1 = control.reader().trim();
                flag = examinationLong(pop1);
                populationDensity = Long.valueOf(pop1);
                if (populationDensity > 0 && flag == true) {
                    flag = true;
                }
            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                flag = false;
            }
        } while (flag == false);

        count = 0;
        flag = false;
        do {
            try {
                if (count == 0) {
                    count += 1;
                    System.out.println("Введите один из 3 климатов OCEANIC, TUNDRA, DESERT: ");

                } else {
                    System.out.println("Неправильный ввод,введите один из 3 климатов OCEANIC, TUNDRA, DESERT: ");
                }
                String search = control.reader().toUpperCase().trim();
                for (Climate enumValue : EnumSet.allOf(Climate.class)) {
                    if (enumValue.name().equals(search)) {
                        flag = true;
                        climate = Climate.valueOf(search);
                        break;
                    }
                }
            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                flag = false;
            }
        } while (flag == false);
        coordinates = new Coordinates((long) x, y);
        city_1 = new City(id, name, coordinates, creationDate, area, population, metersAboveSeaLevel, capital, populationDensity, climate,governor, Controller.user_id);
        request = new CommandsRequest(command_1,Controller.user_id , city_1);
        notId = 0;
//        cities.add(new City(id,name,coordinates,creationDate, area, population, metersAboveSeaLevel,capital, populationDensity ,climate, governor));
        CommandControl.work = 0;
        return "Данные введены корректно";
    }

    public String addIfMaxManager() {
        boolean flag = false;
        int count = 0;
        long maximum = 0;
        int m = 0;
        id = random.nextInt(10 ^ (2 ^ 31)) + 1;


        return (addManager());

    }

    public String updateManager(String line) {
        int count = 0;
        int m = -1;
        try {
            id = Long.parseLong(line);
            if (id <= 0) {
                return "id не может быть меньше или равен 0";
            }

        } catch (NumberFormatException | NullPointerException | java.util.InputMismatchException e) {
            return "Некорректный ввод id";
        }


        return addManager();
    }

    public boolean examinationInt(String stri) {
        int parsedValue = Integer.parseInt(stri, 10);// Проверяем, превышает ли полученное значение максимальную длину
        if (parsedValue > 2147483647) {
            return false;
        } else {
            return true;
        }
    }

    public boolean examinationFloat(String stri) {
        Long parsedValue = Long.parseLong(stri, 10);// Проверяем, превышает ли полученное значение максимальную длину
        if (2147483647 < parsedValue) {
            return false;
        } else {
            return true;
        }
    }


    public boolean examinationLong(String stri) {
        Long parsedValue = Long.parseLong(stri, 10) / 2 ^ 32;// Проверяем, превышает ли полученное значение максимальную длину
        if (2147483647 < parsedValue) {
            return false;
        } else {
            return true;
        }
    }

    public String execuiteScript(String path) {

        try {
            if (path != null) {
                CommandControl.inputData = path;
                CommandControl.count = 0;
                File file = new File(CommandControl.inputData);
                Scanner cv = new Scanner(file);
                workFile.reader(path);
                FilePathLengthCount linePath = new FilePathLengthCount(path, CommandControl.length, 0);
                listPath.add(linePath);
                for (String str : filesName) {
                    if (str.equals(path)) {
                        /*CommandControl.mode=0;
                        CommandControl.work=0;*/
                        for (FilePathLengthCount c : listPath) {
                            c.setLength(0);
                            flag = false;
                        }
                        if (flag == false) {
                            flag = true;
                            filesName.clear();
                            listPath.clear();
                            CommandControl.work=0;
                            CommandControl.mode=0;
                            CommandControl.is = -1;
                            System.out.println( "Произошло зацикливание. Один и тот же файл был вызван несколько раз");
                            Controller controller =new Controller();
                            controller.run();
                            return null;
                        }
                    }
                }
                filesName.add(path);
                /*path[path.length-anInt]=CommandControl.inputData;*/
                /*anInt+=1;*/
                CommandControl.is += 1;
            }
            CommandControl.work = 0;
            CommandControl.mode = 1;
            try {
                control.reader();
                if (listPath.size() == 1 && CollectionManager.listPath.get(CommandControl.is).getCount() >= CollectionManager.listPath.get(CommandControl.is).getLength()) {
                    CommandControl.mode = 0;
                    CommandControl.work = 0;
                    filesName.clear();
                    listPath.clear();
                    CommandControl.is = -1;
                }
                if (CollectionManager.listPath.get(CommandControl.is).getCount() >= CollectionManager.listPath.get(CommandControl.is).getLength()) {
                    listPath.remove(CommandControl.is);
                    filesName.remove(CommandControl.is);
                    CommandControl.is -= 1;
                }

            } catch (java.lang.IndexOutOfBoundsException E) {

            }


        /*for (int i=0;i<CommandControl.length;i+=1){
            control.reader();
        }*/
            if (flag == false) {
                if (listPath.size() == 0) {
                    flag = true;
                    filesName.clear();
                    listPath.clear();
                }
                return "Произошла зацикливание выполнение скрипта было остановлено";
            }

        } catch (java.io.FileNotFoundException e) {
            return "Скрипт не был найден";
        }
        /*int count=0;
        for (int i=0; i < path.length;i+=1) {
            if (path[i] != null) {
                if (path[i].equals(CommandControl.inputData)) {
                    count += 1;
                    if (count == 2) {
                        CommandControl.mode = 0;
                        CommandControl.work = 0;
                        return "Произошло зацикливание";
                    }

                }
            }
        }*/


        /*CommandControl.work=0;
        CommandControl.mode=1;
        while (CommandControl.count<CommandControl.length){
            control.reader();
        }
        *//*for (int i=0;i<CommandControl.length;i+=1){
            control.reader();
        }*//*
        CommandControl.mode=0;
        CommandControl.work=0;
        path=new String[100];
        return "";*/
        return path;
    }
}


