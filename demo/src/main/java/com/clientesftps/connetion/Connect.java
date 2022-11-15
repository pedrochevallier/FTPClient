package com.clientesftps.connetion;

import com.clientesftps.gui.Frame;
//import com.clientesftps.gui.Frame.*;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Connect {
    public static void connect(String serverName, String userName, int port, String password) {
        int reply;
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(serverName, port);
            ftpClient.login(userName, password);

            Frame.setOutput("Connected to " + serverName + ".\n");
            Frame.setOutput(ftpClient.getReplyString() + "\n");

            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                Frame.setOutput("FTP server refused to connect.");
                System.exit(1);
                }
            Files.getFiles(ftpClient, "/");
        }
        catch (IOException io) {
            Frame.setOutput("Connection refused.");
            System.out.println("Connection refused");
            }
        
    }
}