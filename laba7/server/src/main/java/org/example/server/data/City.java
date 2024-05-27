package org.example.server.data;

import java.io.Serializable;
import java.time.LocalDateTime;

public class City implements Serializable {

    private long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float area; //Значение поля должно быть больше 0, Поле не может быть null
    private long population; //Значение поля должно быть больше 0
    private double metersAboveSeaLevel;
    private boolean capital; //Поле не может быть null
    private double populationDensity; //Значение поля должно быть больше 0
    private Climate climate; //Поле не может быть null
    private Human governor; //Поле не может быть null
    private Integer user_id;

    public City(long id, String name, Coordinates coordinates, java.time.LocalDateTime creationDate, float area, long population, double metersAboveSeaLevel, boolean capital, double populationDensity, Climate climate, Human governor,Integer user_id) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = (creationDate);
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.capital = capital;
        this.populationDensity = populationDensity;
        this.climate = climate;
        this.governor = governor;
        this.user_id=user_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public java.time.LocalDateTime getCreationDate() {
        return creationDate;
    }

    public float getArea() {
        return area;
    }

    public long getPopulation() {
        return population;
    }

    public double getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public boolean isCapital() {
        return capital;
    }

    public double getPopulationDensity() {
        return populationDensity;
    }

    public Climate getClimate() {
        return climate;
    }

    public Human getGovernor() {
        return governor;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public void setMetersAboveSeaLevel(double metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public void setPopulationDensity(double populationDensity) {
        this.populationDensity = populationDensity;
    }

    public void setClimate(Climate climate) {
        this.climate = climate;
    }

    public void setGovernor(Human governor) {
        this.governor = governor;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", population=" + population +
                ", metersAboveSeaLevel=" + metersAboveSeaLevel +
                ", capital=" + capital +
                ", populationDensity=" + populationDensity +
                ", climate=" + climate +
                ", governor=" + governor +
                ", user_id=" + user_id +
                '}';
    }
}
