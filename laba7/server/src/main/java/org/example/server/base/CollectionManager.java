package org.example.server.base;
import java.util.Random;
import java.util.*;
import java.lang.NumberFormatException;

import org.example.server.Server;
import org.example.server.data.City;
import org.example.server.data.Climate;
import org.example.server.data.FilePathLengthCount;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.example.server.data.*;
import org.example.server.exceptions.NullPointerException;

import static java.lang.Math.max;
/*import Egor.example.exceptions.NumberFormatException;*/

public class CollectionManager {
    private LocalDateTime time = LocalDateTime.now();
    public static int user_id;
    public static boolean authoriz;
    public static boolean pass;
    private static Integer notId = 0;
    public static LinkedList<City> cities = new LinkedList<>();
    private Converter converter = new Converter();
    private WorkFile workFile = new WorkFile();
    private ReadLine readLine = new ReadLine();
    private boolean flag=true;
    private CommandControl control  =new CommandControl();
    public static ArrayList<String> filesName= new ArrayList<String>();
    public static List<FilePathLengthCount> listPath = new ArrayList<>();
    Random random = new Random();
    public static LinkedList<Users> users =new LinkedList<>();
    private City city_1=null;
    public static int cycle=0;
    private int anInt =1;
    public static Integer user_id_max=0;
    private String[] path=new String[100];
    public static long city_id_max;

    private WorkDataBase worker =new WorkDataBase();
    public String helpManager() {
        return "help: вывести справку по доступным командам\n" + "info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" + "show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" + "add {element}: добавить новый элемент в коллекцию\n" + "update id {element}: обновить значение элемента коллекции, id которого равен заданному\n" + "remove_by_id id: удалить элемент из коллекции по его id\n" + "clear: очистить коллекцию\n" + "save: сохранить коллекцию в файл (используется только на сервере)\n" + "execute_script file_name: считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" + "exit: завершить программу (без сохранения в файл)\n" + "head: вывести первый элемент коллекции\n" + "remove_head: вывести первый элемент коллекции и удалить его\n" + "add_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" + "min_by_capital: вывести любой объект из коллекции, значение поля capital которого является минимальным\n" + "count_less_than_climate climate: вывести количество элементов, значение поля climate которых не равно this.climate\n" + "filter_starts_with_name name: вывести элементы, значение поля name которых содержит заданную подстроку\n"+"out: выйти из аккаунта";
    }
    public String regInManager(String user, String password){
        for (Users user1: users){
            if ((user1.getName()).equals(user)){
                authoriz=false;
                user_id=1;
                return "Данное имя пользователя занято";
            }
        }
        String salt = PasswordUtils.generateSalt();
        worker.addUser(user,password,salt);
        authoriz=true;
        user_id=1;
        user_id_max+=1;
        users.add(new Users(user, password,user_id_max,salt));
        return "Пользователь добавлен";
    }
    public String authorizationManager(String user, String password){
        for (Users user1: users){
            if (user1.getName().equals( user)){
                if (user1.getPassword().equals(password)){
                    authoriz = true;
                    user_id = user1.getId();
                    return "Приветствуем вас, спасибо,что выбираете нас";
                }
            }
        }
        authoriz=false;
        return "Пароль или никнейм был введен неверно";
    }
    public String exitManager() {
        Server.logger.info("Сервер был отключен,a данные в загруженном файле обновлены");
        //saveManager();
        System.exit(0);
        return "Выход";
    }

    public String infoManager() {
        try {
            return "Тип коллекции: " + cities.getClass().getSimpleName() + "\n" + "Внутри лежат элементы типа: " + cities.get(0).getClass() + "\n" + "Количество элементов: " + cities.size() + "\n" + "Дата инициализации: " + time;
        }catch (java.lang.IndexOutOfBoundsException E){
            return "Тип коллекции: " + cities.getClass().getSimpleName() + "\n" + "Количество элементов: " + cities.size() + "\n" + "Дата инициализации: " + time;
        }


    }

    public void sortManager(){
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());            }
        });
    }
    public String showManager() {
        try{

            if (cities.isEmpty() || cities==null) {
                return ("Коллекция городов пуста");
            } else {
                sortManager();
                StringBuilder result = cities.stream()
                        .map(Object::toString)
                        .collect(StringBuilder::new, (sb, str) -> sb.append("\n").append(str), StringBuilder::append);
//                StringBuilder result = new StringBuilder();
//                for (City element : cities) {
//                    result.append("\n");
//                    result.append(element.toString());
//
//                }
                return "В коллекции находятся такие элементы: " + result.toString();
            }
        }catch (java.lang.NullPointerException E){
            return "Колекция городов пустая";
        }

    }

    public String clearManager(Integer user_id) {
        int count = 0;
        int counter =cities.size();
        Iterator<City> iterator = cities.iterator();
        while (iterator.hasNext()) {
            City city1 = iterator.next();
            if (Objects.equals(city1.getUser_id(), user_id)) {
                worker.deleteCityById(city1.getId());
                iterator.remove();

            }else {
                count++;
            }
        }
        if (count==0){
            cities.clear();
            worker.clearCityTable();
            city_id_max=0;
            return "База данных с городами была очищена плностью.";
        }else{
            return "Было удалено "+(counter-count)+" городов";
        }
    }

    public String headManager() {
        try {
            sortManager();
            return "Первый эдлемент: " + cities.get(0).toString();
        }catch (java.lang.IndexOutOfBoundsException E){
            return "Колекция не имеет первого элемента, так как она пустая";
        }

    }

    public String removeHeadManager(Integer id) {
        try{

            sortManager();
            if (Objects.equals(cities.get(0).getUser_id(), id)) {
                String str = cities.get(0).toString();
                worker.deleteCityById(cities.get(0).getId());
                cities.remove(0);
                return "Первый элемент(delete): \n" + str;
            }else{
                return "Данный элемент принадлежит не вам. В удалении отказано";
            }
        }catch (java.lang.IndexOutOfBoundsException E){
            return "Коллекция городов пустая. Первый элемент удален не был";
        }

    }

//    public String saveManager() {
//        sortManager();
//        String json = converter.cityToJson(cities);
//        //String read = readLine.read();
//        workFile.writer(Controller.str, json);
//        return "Сохранен";
//    }

    public String minByCapitalManager() {
        String result =cities.stream().filter(city -> city.isCapital()).findFirst().map(City::toString).orElse(null);
        for (City city : cities) {
            if (city.isCapital()) {
                return "Первый город с капиталом: \n" + city;
            }

        }
        return "Таких городов нет";
    }

    public String filterStartsWithNameManager(String search) {
        sortManager();
        String result = cities.stream()
                .filter(city -> city.getName().toLowerCase().contains(search.toLowerCase()))
                .map(City::toString)
                .collect(Collectors.joining("\n"));
//        String result = "";
//        for (City city : cities) {
//            if (city.getName().toLowerCase().indexOf(search.toLowerCase()) != -1) {
//                result +="\n"+city.toString();
//            }
//        }
        CommandControl.work=0;
        if (result==""){
            return "Данное сочетание не входит не в одно из названий";
        }else {
            return "Вот строки, в которых в имя входит " + search + ":" + result;

        }
    }


    public String countLessThanClimateManager(String search) {

        int hops=0;
        Climate inputEnum = EnumSet.allOf(Climate.class).stream()
                .filter(enumValue -> enumValue.name().equals(search))
                .findFirst()
                .orElse(null);
//        Climate inputEnum = null;
//
//        for (Climate enumValue : EnumSet.allOf(Climate.class)) {
//            if (enumValue.name().equals(search)) {
//
//                inputEnum = Climate.valueOf(search);
//                break;
//            }
//        }
        if(inputEnum==null){
            return "Такого enum не найдено. Enum могли быть: OCEANIC, TUNDRA, DESERT";
        }
        String result = cities.stream()
                .filter(city1 -> !city1.getClimate().equals(inputEnum))
                .map(City::toString)
                .collect(Collectors.joining("\n"));
//        String result = "";
//        for (City city : cities) {
//            if (city.getClimate() == inputEnum) {
//                continue;
//            } else {
//                result += "\n"+city.toString();
//            }
//
//        }
        CommandControl.work=0;
        if (result==""){
            return "У всех городов климат "+inputEnum.name();
        }else {
            return "Вот города у которых климат неравен " + inputEnum.name() + ": " + result;
        }
    }
    public String removeByIdManager(String arg,Integer user_id){
        int index = 0;
        String res = "";
        long id_1=-1;
        try {
            id_1 =Long.parseLong(arg);
        }catch (NumberFormatException e){
            Server.logger.info("Клиент отправил некорректный id "+id_1);
        }


        long finalId_ = id_1;
//        res = cities.stream()
//                .filter(city -> city.getId() == finalId_&& city.getUser_id()==user_id)
//                .peek(city -> cities.remove(city)) // Изменение списка в потоке
//                .map(City::toString)
//                .collect(Collectors.joining());
        for (City city:cities){
            if(city.getId()==id_1 ){
                if (Objects.equals(city.getUser_id(), user_id)){
                    res+=city.toString();
                    worker.deleteCityById(id_1);
                    cities.remove(city);
                    break;
                }else{
                    return "У вас недостаточно прав на удаление данного объекта";
                }


            }

        }



        CommandControl.work=0;
        if (res==""){
            return "Городов с данным id не было найдено";
        }else {
            return "Были удален: " + res;
        }

    }
    public String addManager(City city_1,Integer user_id){
        worker.addCity(city_1);

        city_id_max+=1;
        city_1.setId(city_id_max);
        cities.add(city_1);
        CommandControl.work=0;
        return ("Данные введены корректно, коллекция добавлена");
    }

    public String addIfMaxManager(City city_1){
        boolean flag = false;
        long id_1=city_1.getId();
        int count=0;
        long maximum =0;
        int m=0;
        try {


            for (City city:cities){

                if (maximum<city.getId()){
                    maximum = city.getId();
                }


                if (city.getId() ==id_1){
                    m=-1;
                }

            }

        }catch (NumberFormatException | NullPointerException | java.util.InputMismatchException e){
        }

        CommandControl.work=0;
        if (m==-1){
            return ("Такой id уже существует");
        } else if (maximum>id_1) {
            return ("Есть id больше "+maximum+". Элемент добавлен не будет");
        }else{
            notId=1;
            city_id_max+=1;
            city_1.setId(city_id_max);
            worker.addCity(city_1);
            cities.add(city_1);
            CommandControl.work=0;
            return ("Данные введены корректно, коллекция добавлена");

        }

    }

    public String updateManager(City city_1, Integer user_id){
        int count=0;
        int m=0;
        long id_1=city_1.getId();
//        try {
//            id =Long.parseLong(line);
//            if (id<=0){
//                return "id не может быть меньше или равен 0";
//            }
//
//        }catch (NumberFormatException | NullPointerException | java.util.InputMismatchException e){
//            return "Некорректный ввод id";
//        }

        for (City city:cities ){
            if (city.getId() ==id_1 && Objects.equals(city_1.getUser_id(), user_id)){
                m=-1;
            }

        }

        if (m==-1) {
            for (City city : cities) {
                if (id_1 == city.getId()) {
                    notId=1;
                    CommandControl.work=0;
                    worker.updateCityById(city_1);
                    cities.remove(city);
                    cities.add(city_1);
                    CommandControl.work=0;
                    return ("Данные введены корректно, коллекция обновлена");

                }
            }
        }else{
            CommandControl.work=0;
            return ("Города под данным id не найдено");

        }
        return "";
    }

    public boolean examinationInt(String stri){
        int parsedValue = Integer.parseInt(stri, 10);// Проверяем, превышает ли полученное значение максимальную длину
        if (parsedValue > 2147483647) {
            return false;
        } else {
            return true;
        }
    }

    public boolean examinationFloat(String stri){
        Long parsedValue = Long.parseLong(stri, 10);// Проверяем, превышает ли полученное значение максимальную длину
        if (2147483647 < parsedValue) {
            return false;
        } else {
            return true;
        }
    }


    public boolean examinationLong(String stri){
        Long parsedValue = Long.parseLong(stri, 10)/2^32;// Проверяем, превышает ли полученное значение максимальную длину
        if (2147483647 < parsedValue) {
            return false;
        } else {
            return true;
        }
    }



}
