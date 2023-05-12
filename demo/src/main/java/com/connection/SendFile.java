package com.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;


public class SendFile {

    static FileInputStream fis = null;

    public static void UploadFile(FTPClient ftpClient, String localPath, String serverPath){
        try {
            File secondLocalFile = new File(localPath);
            String secondRemoteFile = serverPath;
            InputStream inputStream = new FileInputStream(secondLocalFile);

            System.out.println("Start uploading second file");
            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;
 
            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();
            outputStream.close();
            boolean completed = ftpClient.completePendingCommand();
            if (completed) {
                System.out.println("The second file is uploaded successfully.");
            }
            System.out.println(completed);

            /* 
            fis = new FileInputStream(localPath);
    
            boolean done = ftpClient.storeFile("sdfsd.docx", fis);
            fis.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            } else {
                System.out.println("Transfer failed");
            }
            */
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

}
