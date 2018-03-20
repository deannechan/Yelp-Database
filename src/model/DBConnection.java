package model;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DBConnection {
	private String databaseUrl;
	private String databaseName;
	private String databaseDriver;
	private String databaseUsername;
	private String databasePassword;
	private Connection connection;

	public DBConnection(String dbHost, String dbPort, String dbName, String dbUsername, String dbPassword) {
		databaseUrl = "jdbc:oracle:thin:@"+dbHost + ":"+dbPort+":";
		databaseDriver = "oracle.jdbc.driver.OracleDriver";
		databaseName = dbName;	//Schema
		databaseUsername = dbUsername;
		databasePassword = dbPassword;
        connection = null;
	}
	
	public DBConnection(){
		databaseUrl = "jdbc:oracle:thin:@192.168.56.2:1521:";
		databaseDriver = "oracle.jdbc.driver.OracleDriver";
		databaseName = "orcl";	//Schema
		databaseUsername = "scott";
		databasePassword = "tiger";
        connection = null;
	}
	
	public Connection getConnection() {
		try {
			Class.forName(databaseDriver).newInstance();
			connection = DriverManager.getConnection(databaseUrl + databaseName, databaseUsername, databasePassword);
			System.out.println("Connected!");
		} catch (Exception e) {
			System.out.println("Error");
			JOptionPane.showMessageDialog(null, "Database not connected!");
			e.printStackTrace();
		}

		return connection;
	}

	public void disconnect() {
		try {
			connection.close();
			System.out.println("Disconnected!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}