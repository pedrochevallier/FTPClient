package com.connection;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.ftp.FTPClientGUI;

public class Connection {
    FTPClient ftpCient;

    public static FTPClient Connect(String host, int port, String user, String password) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setRemoteVerificationEnabled(false);


        try {
            //ftpClient.enterLocalPassiveMode();
            ftpClient.connect(host, port);
            FTPClientGUI.setOutput("Connecting to server...\n");
            ftpClient.login(user, password);
            FTPClientGUI.setOutput("Connection succesfull!\n");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient;
        } catch (SocketException e) {
            FTPClientGUI.setOutput("Connection refused.\n");
            return null;
        } catch (IOException e) {
            FTPClientGUI.setOutput("Somethign went wrong.\n");
            return null;
        }

    }
}
