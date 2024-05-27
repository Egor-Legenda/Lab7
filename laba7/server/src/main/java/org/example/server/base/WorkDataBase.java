package org.example.server.base;

import org.example.server.Server;
import org.example.server.data.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import static java.lang.Math.max;

public class WorkDataBase {
    public void clearCityTable() {
        String query = "TRUNCATE TABLE City CASCADE";
        String resetSequenceQuery = "ALTER SEQUENCE city_id_seq RESTART WITH 1";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(query);
            statement.execute(resetSequenceQuery);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public void deleteCityById(long id) {
        String query = "DELETE FROM City WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            int rowsAffected = statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addCity(City  city_1){
        String query = "INSERT INTO City (name, coordinates, creationDate, area, population, metersAboveSeaLevel, capital, populationDenasity, climate, governor, users_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, city_1.getName());
            statement.setString(2, city_1.getCoordinates().toString());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(city_1.getCreationDate()));
            statement.setFloat(4, city_1.getArea());
            statement.setLong(5, city_1.getPopulation()); // Use setLong for bigint
            statement.setFloat(6, ((Number) city_1.getMetersAboveSeaLevel()).floatValue());
            statement.setBoolean(7, city_1.isCapital());
            statement.setFloat(8, ((Number) city_1.getPopulationDensity()).floatValue());
            statement.setString(9, city_1.getClimate().toString());
            statement.setTimestamp(10, java.sql.Timestamp.valueOf(city_1.getGovernor().getBirthday()));
            statement.setInt(11, city_1.getUser_id()); // Используйте правильный метод для получения users_id

            statement.executeUpdate();

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // SQLState for unique constraint violation
                Server.logger.info("Ошибка: Пользователь с таким именем уже существует.");
            } else {
                e.printStackTrace();
            }
        }
    }
    public void updateCityById(City city) {
        String query = "UPDATE City SET name = ?, coordinates = ?, creationDate = ?, area = ?, population = ?, metersAboveSeaLevel = ?, capital = ?, populationDenasity = ?, climate = ?, governor = ?, users_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, city.getName());
            statement.setString(2, city.getCoordinates().toString()); // Преобразование объекта Coordinates в строку
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(city.getCreationDate()));
            statement.setFloat(4, city.getArea());
            statement.setLong(5, city.getPopulation());
            statement.setDouble(6, city.getMetersAboveSeaLevel());
            statement.setBoolean(7, city.isCapital());
            statement.setDouble(8, city.getPopulationDensity());
            statement.setString(9, city.getClimate().toString()); // Преобразование объекта Climate в строку
            statement.setTimestamp(10, java.sql.Timestamp.valueOf(city.getGovernor().getBirthday())); // Предполагаем, что в Human есть метод getGovernorDate, который возвращает LocalDateTime
            statement.setInt(11, city.getUser_id()); // Предполагаем, что у City есть метод getUserId
            statement.setLong(12, city.getId());

            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка");

        }
    }
    public void addUser(String username, String password, String salt) {
        String query = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, salt);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public void getAllUsers() {


        String query = "SELECT * FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                CollectionManager.user_id_max=max(CollectionManager.user_id_max,id);
                // Создание объекта Users
                Users user = new Users(name, password, id, salt);

                // Добавление объекта Users в список
                CollectionManager.users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getAllCities() {

        String query = "SELECT * FROM city";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String coordinatesString = resultSet.getString("coordinates");
                java.time.LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
                float area = resultSet.getFloat("area");
                long population = resultSet.getLong("population");
                double metersAboveSeaLevel = resultSet.getDouble("metersAboveSeaLevel");
                boolean capital = resultSet.getBoolean("capital");
                double populationDensity = resultSet.getDouble("populationDenasity");
                String climateString = resultSet.getString("climate");
                LocalDateTime governorDate = resultSet.getTimestamp("governor").toLocalDateTime();
                Integer user_id = resultSet.getInt("users_id");
                // Преобразование строки coordinates в объект Coordinates
                Coordinates coordinates = convertStringToCoordinates(coordinatesString);

                // Преобразование строки climate в объект Climate
                Climate climate = Climate.valueOf(climateString.toUpperCase());

                // Создание объекта Human для governor
                Human governor = new Human(governorDate);

                // Создание объекта City
                City city = new City(id, name, coordinates, creationDate, area, population, metersAboveSeaLevel, capital, populationDensity, climate, governor,user_id);
                CollectionManager.city_id_max=max(CollectionManager.city_id_max,id);
                // Добавление объекта City в список
                CollectionManager.cities.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private Coordinates convertStringToCoordinates(String coordinatesString) {
        // Преобразование строки coordinates в объект Coordinates
        // Предположим, что строка имеет формат "x,y"
        String[] parts = coordinatesString.split(", ");
        long x = Long.parseLong(parts[0]);
        double y = Double.parseDouble(parts[1]);
        return new Coordinates(x, y);
    }
}
