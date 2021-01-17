package com.houseofkraft.util;

import java.sql.*;

public class SQL {
    private static final String DRIVER_CLASSPATH = "com.mysql.jdbc.Driver";
    private Connection connection;

    public boolean isClosed() throws SQLException { return connection.isClosed(); }
    public boolean isReadOnly() throws SQLException { return connection.isReadOnly(); }
    public void close() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }

    public void connect(String dbURL, String dbUsername, String dbPassword) throws SQLException {
        this.connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
    }

    public void execute(String command) throws SQLException {
        connection.createStatement().executeUpdate(command);
    }

    public ResultSet query(String queryCommand) throws SQLException {
        return connection.createStatement().executeQuery(queryCommand);
    }

    public SQL(String _dbURL, String _dbUsername, String _dbPassword) throws SQLException {
        super();
        connect(_dbURL, _dbUsername, _dbPassword);
    }

    public SQL() throws ClassNotFoundException {
        Class.forName(DRIVER_CLASSPATH);
    }
}
