package database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBConnection {

    public Connection getCon();
    public void close() throws SQLException;
}
