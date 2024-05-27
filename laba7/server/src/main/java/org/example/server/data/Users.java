package org.example.server.data;

public class Users {
    private String name;
    private String password;
    private Integer id;
    private String salt;

    public Users(String name, String password, Integer id, String salt) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return id;
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return "Users{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", salt='" + salt + '\'' +
                '}';
    }
}
