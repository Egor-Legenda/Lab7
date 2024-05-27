package org.example.server.data;
import java.io.Serializable;
import java.time.LocalDateTime;
public class Human implements Serializable {
    private java.time.LocalDateTime birthday;

    public Human(java.time.LocalDateTime birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Human{" +
                "birthday=" + birthday +
                '}';
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }
}