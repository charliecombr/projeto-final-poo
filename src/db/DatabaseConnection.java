package db;

import java.sql.Connection;

import exception.DatabaseConnectionException;

public interface DatabaseConnection {
    public Connection getConnection() throws DatabaseConnectionException;
    public void disconnect() throws DatabaseConnectionException;
}
