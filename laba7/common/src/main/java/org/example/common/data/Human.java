package org.example.common.data;
import java.io.Serializable;
import java.time.LocalDateTime;
public class Human implements Serializable {
    private LocalDateTime birthday;

    public Human(LocalDateTime birthday) {
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