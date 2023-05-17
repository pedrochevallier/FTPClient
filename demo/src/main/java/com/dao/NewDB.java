package com.dao;

import java.sql.*;


public class NewDB {
    public static void createDataBase(){
        try {
            // Register the H2 JDBC driver
            Class.forName("org.h2.Driver");
            
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:h2:~/Documents/2022/labo1/FTPClientv2/demo/src/main/java/com/db/ftpDB", "admin", "");
   
            // Create a new table
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS CONNECTION " +
                         "(ID INT AUTO_INCREMENT PRIMARY KEY, " +
                         " HOST           VARCHAR(255)    NOT NULL, " +
                         " PORT            INT     NOT NULL, " +
                         " USERNAME        VARCHAR(255) NOT NULL, " +
                         " PASSWORD       VARCHAR(20) NOT NULL);";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully");
            
            // Close the connection
            conn.close();
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
    }
}
