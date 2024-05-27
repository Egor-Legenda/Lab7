package org.example.common.request;

import org.example.common.data.City;
import org.example.common.data.Climate;

public class CommandsRequest extends Request {
    public Integer id;
    public City city;
    public String line;
    public Climate climate;
    public String user;
    public String password;
    public CommandsRequest(String name, String user,String password){
        super(name);
        this.user=user;
        this.password=password;
    }
    public CommandsRequest(String name) {
        super(name);
    }
    public CommandsRequest(String name, Integer id){
        super(name);
        this.id=id;
    }
    public CommandsRequest(String name, Integer id, City city){
        super(name);
        this.id=id;
        this.city=city;
    }
    public CommandsRequest(String name, Climate climate){
        super(name);
        this.climate=climate;
    }
    public CommandsRequest(String name, String line){
        super(name);
        this.line=line;
    }
    public CommandsRequest(String name, String line, Integer user_id){
        super(name);
        this.line=line;
        this.id=user_id;
    }

    public Integer getId() {
        return id;
    }

    public City getCity() {
        return city;
    }

    public String getLine() {
        return line;
    }

    public Climate getClimate() {
        return climate;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
