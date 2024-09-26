package dn.rubtsov.parserj_02.processor;

import dn.rubtsov.parserj_02.data.Header;
import dn.rubtsov.parserj_02.dto.MessageDB;
import dn.rubtsov.parserj_02.data.Registers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    //"jdbc:postgresql://postgres:5432/postgres"; - если БД только в Docker
    public static final String USER = "postgres";
    public static final String PASSWORD = "1";

    /**
     Метод создания таблицы message_db БД, если она не существует.
     */
    public static void createTableIfNotExists(String tableName) {

        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+ tableName +" (" +
                "uid UUID PRIMARY KEY DEFAULT gen_random_uuid(), " +
                "insert_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(), " +
                "product_id VARCHAR(60) NOT NULL, " +
                "message_id VARCHAR(60) NOT NULL, " +
                "accounting_date VARCHAR(60) NOT NULL, " +
                "register_type VARCHAR(60) NOT NULL, " +
                "rest_in INTEGER NOT NULL" +
                "dispatchStatus INTEGER DEFAULT 0" +
                ");";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     Метод записи объектов списка MessageDB в таблицу БД.
     */
    public static void insertRecords(List<MessageDB> data) {
        String insertDataSQL = "INSERT INTO message_db " +
                "(product_id, message_id, accounting_date, register_type, rest_in) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {

            for (MessageDB entry : data) {
                preparedStatement.setString(1, entry.getHeader().getProductId());
                preparedStatement.setString(2, entry.getHeader().getMessageId());
                preparedStatement.setString(3, entry.getHeader().getAccountingDate());
                preparedStatement.setString(4, entry.getRegisters().getRegisterType());
                preparedStatement.setInt(5, entry.getRegisters().getRestIn());
                preparedStatement.addBatch(); // Добавление в пакет
            }

            preparedStatement.executeBatch(); // Выполнение всех вставок за один раз

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     Метод получения всех записей из таблицы БД.
     */
    public static List<MessageDB> selectAllRecords() {
        List<MessageDB> messagesList = new ArrayList<>();
        String selectSQL = "SELECT " +
                "product_id, message_id, accounting_date, register_type, rest_in " +
                "FROM message_db";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String productId = resultSet.getString("product_id");
                String messageId = resultSet.getString("message_id");
                String accountingDate = resultSet.getString("accounting_date");
                String registerType = resultSet.getString("register_type");
                int restIn = resultSet.getInt("rest_in");

                // Создание объекта MessageDB и добавление в список
                Header header = new Header();
                header.setProductId(productId);
                header.setMessageId(messageId);
                header.setAccountingDate(accountingDate);

                Registers registers = new Registers();
                registers.setRegisterType(registerType);
                registers.setRestIn(restIn);

                MessageDB messageDB = new MessageDB();
                messageDB.setHeader(header);
                messageDB.setRegisters(registers);
                messagesList.add(messageDB);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messagesList;
    }
    /**
     Метод удаления таблицы registers из БД.
     */
    public static void dropTableIfExists(String tableName) {
        String dropDataSQL = "DROP TABLE " + tableName;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(dropDataSQL)) {
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод для получения первой записи с dispatchStatus = 0
     * и обновления dispatchStatus на 1.
     */
    public static MessageDB getAndUpdateFirstRecordWithDispatchStatus() {
        MessageDB messageDB = null;
        String selectSQL = "SELECT product_id, message_id, accounting_date, register_type, rest_in " +
                "FROM message_db WHERE dispatchStatus = 0 LIMIT 1";
        String updateSQL = "UPDATE message_db SET dispatchStatus = 1 WHERE register_type = ? AND rest_in = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
             PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
             ResultSet resultSet = selectStatement.executeQuery()) {

            // Выборка первой записи
            if (resultSet.next()) {
                String productId = resultSet.getString("product_id");
                String messageId = resultSet.getString("message_id");
                String accountingDate = resultSet.getString("accounting_date");
                String registerType = resultSet.getString("register_type");
                int restIn = resultSet.getInt("rest_in");

                // Создание объекта MessageDB
                Header header = new Header();
                header.setProductId(productId);
                header.setMessageId(messageId);
                header.setAccountingDate(accountingDate);

                Registers registers = new Registers();
                registers.setRegisterType(registerType);
                registers.setRestIn(restIn);

                messageDB = new MessageDB();
                messageDB.setHeader(header);
                messageDB.setRegisters(registers);

                // Обновление значения dispatchStatus
                updateStatement.setString(1, registerType);
                updateStatement.setInt(2,restIn);
                updateStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageDB;
    }

}
