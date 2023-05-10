package com.connection;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

public class Connection {
    FTPClient ftpCient;

    public static FTPClient Connect(String host, int port, String user, String password) {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(host, port);
            System.out.println("Connecting to server...");
            ftpClient.login(user, password);
            System.out.println("Connection succesfull!");
            return ftpClient;
        } catch (SocketException e) {
            System.out.println("Connection refused.");
            return null;
        } catch (IOException e) {
            System.out.println("Somethign went wrong.");
            return null;
        }
        
    }
}