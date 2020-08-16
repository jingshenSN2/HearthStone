package sn2.heartstone.data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite implements SQLAccessor{

    private String dbName;
    private String dbPath;
    private boolean hasPath = false;
    
    private Connection dbConnection;
    private String dbUrl;
    
    public SQLite(String name) {
		this.dbName = name;
	}
    
    public SQLite(String path, String name) {
		this.dbName = name;
		this.dbPath = path;
		this.hasPath = true;
	}
    
	@Override
	public void connect() {
		if (this.hasPath)
			dbUrl = "jdbc:sqlite:" + this.dbPath + "/" + this.dbName + ".db";
		else
			dbUrl = "jdbc:sqlite:" + this.dbName + ".db";
		try {
			Class.forName("org.sqlite.JDBC");
			this.dbConnection = DriverManager.getConnection(dbUrl);
		} catch (Exception e) {
			System.out.println("[SQLITE] An error occurred while connecting.");
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		try {
			if (!this.dbConnection.isClosed())
				this.dbConnection.close();
			else 
				System.out.println("[SQLITE] Already closed.");
		} catch (SQLException e) {
			System.out.println("[SQLITE] An error occurred while disconnecting.");
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		try {
			return !this.dbConnection.isClosed();
		} catch (SQLException e) {
			System.out.println("[SQLITE] An error occurred while connecting.");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ResultSet getResult(String command) {
		try {
			if (this.dbConnection.isClosed())
				this.connect();
			Statement statement = this.dbConnection.createStatement();
			return statement.executeQuery(command);
		} catch (SQLException e) {
			System.out.println("[SQLITE] An error occurred while executing the command.");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResultSet getResultPreparedStatement(String command) {
		try {
			if (this.dbConnection.isClosed())
				this.connect();
			PreparedStatement statement = this.dbConnection.prepareStatement(command);
			return statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("[SQLITE] There was an error while executing the command.");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void executeCommand(String command) {
		try {
			if (this.dbConnection.isClosed())
				this.connect();
			Statement statement = this.dbConnection.createStatement();
			statement.executeUpdate(command);
		} catch (SQLException e) {
			System.out.println("[SQLITE] An error occurred while executing the command.");
			e.printStackTrace();
		}
	}

	@Override
	public PreparedStatement executeCommandPreparedStatement(String command) {
		try {
			if (this.dbConnection.isClosed())
				this.connect();
			return this.dbConnection.prepareStatement(command);
		} catch (SQLException e) {
			System.out.println("[SQLITE] There was an error while executing the command.");
			e.printStackTrace();
		}
		return null;
	}
	
}
