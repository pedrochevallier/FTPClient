package com.dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.entity.Connection;

public class DAOConnection implements IDAO<Connection> {
    private String DB_JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:~/Documents/2022/labo1/FTPClientv2/demo/src/main/java/com/db/ftpDB";
    private String DB_USER = "admin";
    private String DB_PASSWORD = "";

    @Override
    public void save(Connection element) throws DAOException {
        try {
            java.sql.Connection conn = null;
            PreparedStatement preparedStatement = null;
            Class.forName(DB_JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = conn.prepareStatement("INSERT INTO CONNECTION (HOST, PORT, USERNAME, PASSWORD) " +
            "SELECT ?, ?, ?, ? " +
            "WHERE NOT EXISTS (" +
            "    SELECT * FROM CONNECTION " +
            "    WHERE HOST = ? AND PORT = ? AND USERNAME = ? AND PASSWORD = ?);");

            preparedStatement.setString(1, element.getHost());
            preparedStatement.setInt(2, element.getPort());
            preparedStatement.setString(3, element.getUserName());
            preparedStatement.setString(4, element.getPassword());

            preparedStatement.setString(5, element.getHost());
            preparedStatement.setInt(6, element.getPort());
            preparedStatement.setString(7, element.getUserName());
            preparedStatement.setString(8, element.getPassword());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    
    @Override
    public void delete(int id) throws DAOException{
        try {
            java.sql.Connection conn = null;
            PreparedStatement preparedStatement = null;
            Class.forName(DB_JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = conn.prepareStatement("DELETE FROM CONNECTION WHERE ID = ?;");

            preparedStatement.setInt(1, id);
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
    
    @Override
    public ArrayList<Connection> searchAll() throws DAOException {
        java.sql.Connection conn = null;
        Connection connection = null;
        ArrayList<Connection> allConnections = new ArrayList<>();
        try{
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CONNECTION");
            while(rs.next()){
                connection = new Connection();
                connection.setId(rs.getInt("ID"));
                connection.setHost(rs.getString("HOST"));
                connection.setPort(rs.getInt("PORT"));
                connection.setUserName(rs.getString("USERNAME"));
                connection.setPassword(rs.getString("PASSWORD"));

                allConnections.add(connection);
            }
            rs.close();
            stmt.close();
            conn.close();
            
        }
        catch (ClassNotFoundException | SQLException e){
            throw new DAOException(e.getMessage());
        }
        return allConnections;
    }
}
