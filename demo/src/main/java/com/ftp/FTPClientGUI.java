package com.ftp;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;

import com.files.FTPFileTree;
import com.files.LocalFileTree;

public class FTPClientGUI extends JFrame {
    private JLabel hostLabel;
    private JTextField hostField;
    private JLabel portLabel;
    private JTextField portField;
    private JLabel userLabel;
    private JTextField userField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton connectButton;
    private JButton disconnectButton;
    private JTree localTree;
    private JTree serverTree;
    private JButton downloadButton;
    private JButton uploadButton;

    public FTPClientGUI() throws IOException {
        // Initialize the components
        hostLabel = new JLabel("FTP Host:");
        hostField = new JTextField("localhost");
        portLabel = new JLabel("FTP Port: ");
        portField = new JTextField("2121");
        userLabel = new JLabel("Username:");
        userField = new JTextField("pedro");
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField("1234");
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        downloadButton = new JButton("Download");
        uploadButton = new JButton("Upload");

        // Create the local file system tree
        DefaultMutableTreeNode localRoot = new LocalFileTree().getRoot();
        DefaultTreeModel localModel = new DefaultTreeModel(localRoot);
        localTree = new JTree(localModel);
        localTree.setRootVisible(false);
        JScrollPane localScrollPane = new JScrollPane(localTree);
        localScrollPane.setBorder(BorderFactory.createTitledBorder("Local Files"));
        
        // Create the remote file system tree
        final FTPFileTree ftpFileTree = new FTPFileTree("192.168.1.52", 2121, "pedro", "1234",1);
        DefaultMutableTreeNode serverRoot = ftpFileTree.getRoot();
        DefaultTreeModel serverModel = new DefaultTreeModel(serverRoot);
        serverTree = new JTree(serverModel);
        serverTree.setRootVisible(false);
        JScrollPane serverScrollPane = new JScrollPane(serverTree);
        serverScrollPane.setBorder(BorderFactory.createTitledBorder("Server Files"));

        serverTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        
                try {
                    ftpFileTree.loadChildren(node, 0);
                    ((DefaultTreeModel) serverTree.getModel()).reload(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                // Do nothing
            }
        });

        // Set the layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Add the components to the frame
        c.gridx = 0;
        c.gridy = 0;
        add(hostLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(hostField, c);

        c.gridx = 0;
        c.gridy = 1;
        add(userLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        add(userField, c);

        c.gridx = 0;
        c.gridy = 2;
        add(passwordLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        add(passwordField, c);

        c.gridx = 0;
        c.gridy = 3;
        add(connectButton, c);

        c.gridx = 1;
        c.gridy = 3;
        add(disconnectButton, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(localScrollPane, c);

        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(serverScrollPane, c);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0;
        add(downloadButton, c);

        c.gridx = 2;
        c.gridy = 5;
        add(uploadButton, c);
        
        //add listener to connect button
        connectButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String host = hostField.getText();
                String user = userField.getText();
                Integer port = Integer.parseInt(portField.getText());
                String password = String.valueOf(passwordField.getPassword());
                           
            }

        });

        // Set the frame properties
        // Set the frame properties
        setTitle("FTP Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        // Create the GUI instance
        new FTPClientGUI();
    }
}