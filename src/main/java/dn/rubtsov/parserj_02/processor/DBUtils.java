package dn.rubtsov.parserj_02.processor;

import dn.rubtsov.parserj_02.data.Registers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DBUtils {
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "1";
    private ParserJson parserJson;

    public static void insertRecord (List<Registers> data) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            data.forEach(entry -> {
                try {
                    String insertData = "INSERT INTO registers(register_type, rest_in) VALUES (?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertData);
                    preparedStatement.setString(1, entry.getRegisterType());
                    preparedStatement.setInt(2, entry.getRestIn());
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
