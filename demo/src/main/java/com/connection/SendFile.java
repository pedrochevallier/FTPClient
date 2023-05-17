package com.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.gui.FTPClientGUI;


public class SendFile {

    static FileInputStream fis = null;

    public static void UploadFile(FTPClient ftpClient, String localPath, String serverPath){
        try {
            File secondLocalFile = new File(localPath);
            String secondRemoteFile = serverPath;
            InputStream inputStream = new FileInputStream(secondLocalFile);

            FTPClientGUI.setOutput("Uploading file");
            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;
 
            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
                FTPClientGUI.setOutput(".");
            }
            FTPClientGUI.setOutput("\n");
            inputStream.close();
            outputStream.close();
            boolean completed = ftpClient.completePendingCommand();
            if (completed) {
                FTPClientGUI.setOutput("file uploaded successfully.\n");
            }else{
                FTPClientGUI.setOutput("Somethign went wrong uploading the file\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

}
