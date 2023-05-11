package com.ftp;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;

import org.apache.commons.net.ftp.FTPClient;

import com.connection.Connection;
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
    private JScrollPane serverScrollPane;
    private JButton downloadButton;
    private JButton uploadButton;
    private JTextArea output;

    private FTPClient ftpClient;
    private FTPFileTree ftpFileTree;

    public FTPClientGUI() throws IOException {
        // Initialize the components
        hostLabel = new JLabel("FTP Host:");
        hostField = new JTextField("192.168.1.52");
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
        output = new JTextArea();

        // Create the local file system tree
        DefaultMutableTreeNode localRoot = new LocalFileTree().getRoot();
        DefaultTreeModel localModel = new DefaultTreeModel(localRoot);
        localTree = new JTree(localModel);
        localTree.setRootVisible(false);
        JScrollPane localScrollPane = new JScrollPane(localTree);
        localScrollPane.setBorder(BorderFactory.createTitledBorder("Local Files"));

        // Create the remote file system tree
        DefaultMutableTreeNode serverRoot = new DefaultMutableTreeNode("Connect to display files");
        serverTree = new JTree(serverRoot);
        serverScrollPane = new JScrollPane(serverTree);
        serverScrollPane.setBorder(BorderFactory.createTitledBorder("Server Files"));

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
        add(portLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        add(portField, c);

        c.gridx = 0;
        c.gridy = 2;
        add(userLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        add(userField, c);

        c.gridx = 0;
        c.gridy = 3;
        add(passwordLabel, c);

        c.gridx = 1;
        c.gridy = 3;
        add(passwordField, c);

        c.gridx = 0;
        c.gridy = 4;
        add(connectButton, c);

        c.gridx = 1;
        c.gridy = 4;
        add(disconnectButton, c);
        disconnectButton.setEnabled(false);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(localScrollPane, c);

        c.gridx = 2;
        c.gridy = 5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(serverScrollPane, c);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0;
        add(downloadButton, c);

        c.gridx = 2;
        c.gridy = 6;
        add(uploadButton, c);

        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 3;
        c.gridheight = 4;
        add(output, c);

        // add listener to connect button
        connectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String host = hostField.getText();
                String user = userField.getText();
                Integer port = Integer.parseInt(portField.getText());
                String password = String.valueOf(passwordField.getPassword());

                try {
                    ftpClient = Connection.Connect(host, port, user, password);
                    if(ftpClient == null){
                        return;
                    }else{
                        connectButton.setEnabled(false);
                        disconnectButton.setEnabled(true);
                        ftpFileTree = new FTPFileTree(host, ftpClient, 1);
                        serverTree.setModel(new DefaultTreeModel(ftpFileTree.getRoot()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // Action listener for the disconnect button
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    ftpClient.disconnect();
                    System.out.println("Disconneted from server.");
                    disconnectButton.setEnabled(false);
                    connectButton.setEnabled(true);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

        // si quiero no hacerlo de forma recursiva habilito el actionListener
        
        // Action listener for the nodes in the tree
        serverTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath node = (TreePath) event.getPath();
                System.out.println(node);
                /*
                 
                try {
                    ftpFileTree.buildTree(node);
                    ((DefaultTreeModel) serverTree.getModel()).reload(node);
                } catch (IOException e) {
                    System.out.println(e);
                }
                 */
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath node = (TreePath) event.getPath();
                System.out.println(node);
            }
            
        }); 
        
        
        

        // Set the frame properties
        setTitle("FTP Client");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        // Create the GUI instance
        new FTPClientGUI();
    }
}