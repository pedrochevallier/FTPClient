package com.clientesftps.connetion;

import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;

import javax.swing.tree.DefaultMutableTreeNode;

import com.clientesftps.gui.Frame;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class Files {

    public String filename;
    public Boolean isDirectory;

    public static void getFiles(FTPClient ftpClient, String directory) throws IOException{
        FTPFile[] files = ftpClient.listFiles(directory);
        //System.out.println(ftpClient.printWorkingDirectory());

        // iterates over the files and prints details for each
        //DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(FTPFile file:files){
            String details = file.getName();
            Files detailss = new Files();
                        
            if (file.isDirectory()) {
                detailss.filename = details;
                detailss.isDirectory = true;
                }
            else{
                detailss.filename = details;
                detailss.isDirectory = false;
            }
            
            //details += "\t\t" + file.getSize();
            //details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
            Frame.setFiles(ftpClient ,detailss);
            }
    }

    public static void getChild(FTPClient ftpClient, DefaultMutableTreeNode parent, String directory) throws IOException{
        FTPFile[] files = ftpClient.listFiles(directory);
        for(FTPFile file:files){
            String details = file.getName();
            Files detailss = new Files();
            
            detailss.filename = details;
            detailss.isDirectory = file.isDirectory();
            Frame.setChild(parent ,detailss);
            }
    }
}

