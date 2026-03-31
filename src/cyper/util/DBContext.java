package cyper.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static String URl = "jdbc:mysql://localhost:3306/cyber_management";
    private static String User = "root";
    private static String Password = "09042006";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URl, User, Password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
