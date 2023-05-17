package com.connection;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.dao.DAOConnection;
import com.dao.DAOException;
import com.entity.Connection;
import com.gui.FTPClientGUI;

public class Connect {
    FTPClient ftpCient;

    public static FTPClient connect(String host, int port, String user, String password) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setRemoteVerificationEnabled(false);


        try {
            //ftpClient.enterLocalPassiveMode();
            ftpClient.connect(host, port);
            FTPClientGUI.setOutput("Connecting to server...\n");
            ftpClient.login(user, password);
            FTPClientGUI.setOutput("Connection succesfull!\n");

            // if connection is succesfull the log in details are saved to te database
            Connection element = new Connection();
            element.setHost(host);
            element.setPort(port);
            element.setUserName(user);
            DAOConnection con = new DAOConnection();
            try {
                con.save(element);
            } catch (DAOException e) {
                e.printStackTrace();
            }
            
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
