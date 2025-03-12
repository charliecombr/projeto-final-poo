package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exception.DatabaseConnectionException;
import util.LoadParameter;

public class MySQLDatabaseConnection implements DatabaseConnection {

    private Connection conn;
    private final String DBNAME;
    private final String DBADDRESS;
    private final String DBPASSWORD;
    private final String DBPORT;
    private final String DBUSER;

    public MySQLDatabaseConnection() throws DatabaseConnectionException {
        DBNAME = LoadParameter.getValor("DBNAME");
        DBADDRESS = LoadParameter.getValor("DBADDRESS");
        DBPASSWORD = LoadParameter.getValor("DBPASSWORD");
        DBPORT = LoadParameter.getValor("DBPORT");
        DBUSER = LoadParameter.getValor("DBUSER");
    }

    @Override
    public Connection getConnection() throws DatabaseConnectionException {
        try {
			if (conn == null || conn.isClosed()) {
			    try {
			        conn = DriverManager.getConnection("jdbc:mysql://" + DBADDRESS + ":" + DBPORT + "/" + DBNAME, DBUSER, DBPASSWORD);
			    } catch (SQLException e) {
			        throw new DatabaseConnectionException("Erro ao conectar ao banco de dados MySQL: " + e.getMessage(), e);
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return conn;
    }

    @Override
    public void disconnect() throws DatabaseConnectionException {
        try {
			if (conn != null && !conn.isClosed()) {
			    try {
			        conn.close();
			        conn = null;
			    } catch (SQLException e) {
			        throw new DatabaseConnectionException("Erro ao desconectar do banco de dados: " + e.getMessage(), e);
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}