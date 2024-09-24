package dn.rubtsov.parserj_02.processor;

import dn.rubtsov.parserj_02.data.Registers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "1";

    /**
     Метод создания таблицы registers БД, если она не существует.
     */
    public static void createTableIfNotExists(String tableName) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+ tableName +" (" +
                "id SERIAL PRIMARY KEY, " +
                "register_type VARCHAR(255) NOT NULL, " +
                "rest_in INTEGER NOT NULL" +
                ");";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     Метод записи объектов списка Registers в таблицу БД.
     */
    public static void insertRecords(List<Registers> data) {
        String insertDataSQL = "INSERT INTO registers (register_type, rest_in) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {

            for (Registers entry : data) {
                preparedStatement.setString(1, entry.getRegisterType());
                preparedStatement.setInt(2, entry.getRestIn());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     Метод получения всех записей из таблицы БД.
     */
    public static List<Registers> selectAllRecords() {
        List<Registers> registersList = new ArrayList<>();
        String selectSQL = "SELECT register_type, rest_in FROM registers";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String registerType = resultSet.getString("register_type");
                int restIn = resultSet.getInt("rest_in");

                // Создание объекта Registers и добавление в список
                Registers registers = new Registers();
                registers.setRegisterType(registerType);
                registers.setRestIn(restIn);
                registersList.add(registers);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return registersList;
    }
}
