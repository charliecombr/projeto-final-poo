package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.LoadParameter;

public class MySQLDatabaseConnection implements DatabaseConnection{

	private Connection conn;
	private final String DBNAME = LoadParameter.getValor("DBNAME");
	private final String DBADDRESS = LoadParameter.getValor("DBADDRESS");
	private final String DBPASSWORD = LoadParameter.getValor("DBPASSWORD");
	private final String DBPORT = LoadParameter.getValor("DBPORT");
	private final String DBUSER = LoadParameter.getValor("DBUSER");
	

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://"+DBADDRESS+":"+DBPORT+"/"+DBNAME, DBUSER, DBPASSWORD);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			if(conn != null && !conn.isClosed())
				conn = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
