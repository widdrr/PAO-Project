package database;

import java.sql.*;
import java.util.List;

public class MySQLConnection implements IDBConnection{

    private final String URL = "jdbc:mysql://localhost:3306/java_project";
    private final String USER = "root";
    private final String PASSWORD = "root1337";
    private Connection conn;
    static MySQLConnection instance = null;

    private MySQLConnection(){
        try {
            this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static public MySQLConnection getInstance() {
        if (instance == null){
            instance = new MySQLConnection();
        }
        return instance;
    }

    @Override
    public Connection getCon() {
        return conn;
    }

    @Override
    public void close() throws SQLException{
            conn.close();
    }
}
