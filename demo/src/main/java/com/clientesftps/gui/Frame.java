package com.clientesftps.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.net.DefaultDatagramSocketFactory;
import org.apache.commons.net.ftp.FTPClient;

import com.clientesftps.connetion.Connect;
import com.clientesftps.connetion.Files;;

public class Frame extends JFrame{

    //construct preComponent
    static String[] serverFilesItems = {};

    //construct components
    private JTextField userHost = new JTextField("localhost");
    private JTextField userName = new JTextField("pedro");
    private JTextField userPort = new JTextField("2121");
    private JPasswordField userPassword = new JPasswordField("1234");
    private static JTextArea output = new JTextArea();

    //construct tree and panel
    private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
    private static JTree serverFiles = new JTree(root);
    private JScrollPane scroll = new JScrollPane(serverFiles, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private LocalFS localFiles = new LocalFS();

    //construct labels
    private JLabel userHostLabel = new JLabel("Host");
    private JLabel userNameLabel = new JLabel("Username");
    private JLabel userPortLabel = new JLabel("Port");
    private JLabel userPasswordLabel = new JLabel("Password");

    //construct buttons
    private JButton connectButton = new JButton("Connect");    
    

    public Frame(){
        this.setTitle("FTP Client");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(1200,800);
        this.setLayout(null);
        this.setVisible(true);

        //components size and location
        userHost.setBounds(50,45,100,20);
        userName.setBounds(160,45,100,20);
        userPort.setBounds(270,45,100,20);
        userPassword.setBounds(380,45,100,20);
        connectButton.setBounds(495,45,100,20);
        output.setBounds(70,700,655,40);

        localFiles.setLocation(50, 150);
        localFiles.setSize(500,500);

        scroll.setSize(500,500);
        scroll.setLocation(640, 150);
        

        //labels size and location
        userHostLabel.setBounds(50,25,100,20);
        userNameLabel.setBounds(160,25,100,20);
        userPortLabel.setBounds(270,25,100,20);
        userPasswordLabel.setBounds(380,25,100,20);

        //add components
        this.add(userHost);
        this.add(userHostLabel);
        this.add(userName);
        this.add(userNameLabel);
        this.add(userPort);
        this.add(userPortLabel);
        this.add(userPassword);
        this.add(userPasswordLabel);
        this.add(connectButton);
        this.add(output);
        this.add(scroll);
        this.add(localFiles);

        

        //add listener to connect button
        connectButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String host = userHost.getText();
                String user = userName.getText();
                Integer port = Integer.parseInt(userPort.getText());
                String password = String.valueOf(userPassword.getPassword());
                Connect.connect(host, user, port, password);                
            }

        }); 
    }
    

    //set text area input
    public static void setOutput(String detail){
        output.append(detail);
    }

    //set files to tree
    public static void setFiles(FTPClient ftpClient , Files file) throws IOException{

        DefaultTreeModel model = (DefaultTreeModel) serverFiles.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) serverFiles.getModel().getRoot();
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.filename);
        model.insertNodeInto(child, root, root.getChildCount());
        if(file.isDirectory == true){
            Files.getChild(ftpClient, child, "/" + file.filename);
        }
        serverFiles.scrollPathToVisible(new TreePath(child.getPath()));
    }

    public static void setChild(DefaultMutableTreeNode parent, Files file) throws IOException{
        DefaultTreeModel model = (DefaultTreeModel) serverFiles.getModel();
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.filename);
        model.insertNodeInto(child, parent, parent.getChildCount());
        if(file.isDirectory == true){
            
        }
    }
}

