package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerConnUtils_JTDS {
	public static Connection getSQLServerConnection()
				throws SQLException, ClassNotFoundException {
		String hostName = "localhost";
		String sqlInstanceName = "MSSQLSERVER";
		String database = "Tetris";
		String userName = "sa";
		String password = "1234";
		
		return getSQLServerConnection(hostName, sqlInstanceName, database,
					userName, password);
	}
	
	public static Connection getSQLServerConnection(String hostName,
			String sqlInstanceName, String database, String userName,
			String password) throws ClassNotFoundException, SQLException {
		
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		
		String connectionURL = "jdbc:jtds:sqlserver://" + hostName + ":1433/"
				+ database + ";instance=" +sqlInstanceName;
		
		Connection conn = DriverManager.getConnection(connectionURL,userName,
				password);
		return conn;
	}
}
