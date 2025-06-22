import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:mydatabase.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
