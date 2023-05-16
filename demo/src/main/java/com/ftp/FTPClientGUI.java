package com.ftp;

import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import org.apache.commons.net.ftp.FTPClient;

import com.connection.Connection;
import com.connection.SendFile;
import com.files.FTPFileTree;
import com.files.GetPath;
import com.files.LocalFileTree;

public class FTPClientGUI extends JFrame {
    private JLabel hostLabel;
    private JLabel portLabel;
    private JLabel passwordLabel;
    private JLabel userLabel;
    private JTextField hostField;
    private JTextField portField;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton downloadButton;
    private JButton localDeleteButton;
    private JButton newFolderButton;
    private JButton uploadButton;
    private JButton serverDeleteButton;
    private JButton reloadButton;
    private static JTextArea output;
    private JButton renameButton;

    JScrollPane localScrollPane;
    private JScrollPane serverScrollPane;
    private JTree serverTree;
    private JTree localTree;

    private LocalFileTree localFileTree;

    private FTPClient ftpClient;
    private FTPFileTree ftpFileTree;

    private TreePath localPath;
    private TreePath serverPath;
    private TreePath localDirPath;
    private TreePath localFilePath;
    private TreePath serverDirPath;
    private TreePath serverFilePath;

    private String localPathS;
    private String serverPathS;

    public FTPClientGUI() throws IOException {
        // Initialize the components
        hostLabel = new JLabel("FTP Host");
        portLabel = new JLabel("FTP Port");
        passwordLabel = new JLabel("Password");
        userLabel = new JLabel("Username");
        hostField = new JTextField(5);
        portField = new JTextField(5);
        userField = new JTextField(5);
        passwordField = new JPasswordField(5);
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        downloadButton = new JButton("Download");
        localDeleteButton = new JButton("Delete");
        newFolderButton = new JButton("New Folder");
        uploadButton = new JButton("Upload");
        serverDeleteButton = new JButton("Delete");
        reloadButton = new JButton("Reload");
        renameButton = new JButton("Rename");
        output = new JTextArea(5, 5);

        output.setBorder(BorderFactory.createTitledBorder("Output"));

        // Create the local file system tree
        localFileTree = new LocalFileTree();
        DefaultMutableTreeNode localRoot = localFileTree.getRoot();
        DefaultTreeModel localModel = new DefaultTreeModel(localRoot);
        localTree = new JTree(localModel);
        localTree.setRootVisible(true);
        localScrollPane = new JScrollPane(localTree);
        localScrollPane.setBorder(BorderFactory.createTitledBorder("Local Files"));

        // Create the remote file system tree
        DefaultMutableTreeNode serverRoot = new DefaultMutableTreeNode("Connect to display files");
        serverTree = new JTree(serverRoot);
        serverScrollPane = new JScrollPane(serverTree);
        serverScrollPane.setBorder(BorderFactory.createTitledBorder("Server Files"));

        // Set the layout
        setLayout(null);

        // Add the components to the frame
        add(hostLabel);
        add(portLabel);
        add(passwordLabel);
        add(userLabel);
        add(hostField);
        add(portField);
        add(userField);
        add(passwordField);
        add(connectButton);
        add(disconnectButton);
        add(downloadButton);
        add(localDeleteButton);
        add(newFolderButton);
        add(uploadButton);
        add(serverDeleteButton);
        add(reloadButton);
        add(output);
        add(renameButton);
        add(localScrollPane);
        add(serverScrollPane);

        // labels
        hostLabel.setBounds(70, 5, 100, 25);
        portLabel.setBounds(185, 5, 100, 25);
        passwordLabel.setBounds(395, 5, 100, 25);
        userLabel.setBounds(285, 5, 100, 25);
        // connect inputs
        hostField.setBounds(50, 30, 100, 25);
        portField.setBounds(160, 30, 100, 25);
        userField.setBounds(270, 30, 100, 25);
        passwordField.setBounds(380, 30, 100, 25);
        // connect - disconnect buttons
        connectButton.setBounds(495, 30, 100, 25);
        disconnectButton.setBounds(610, 30, 100, 25);
        // file trees
        localScrollPane.setBounds(50, 70, 400, 550);
        serverScrollPane.setBounds(550, 70, 400, 550);
        // local tree buttons
        downloadButton.setBounds(50, 665, 100, 25);
        localDeleteButton.setBounds(50, 695, 100, 25);
        renameButton.setBounds(160, 665, 100, 25);
        // server tree buttons
        newFolderButton.setBounds(550, 695, 100, 25);
        uploadButton.setBounds(550, 665, 100, 25);
        serverDeleteButton.setBounds(660, 665, 100, 25);
        reloadButton.setBounds(660, 695, 100, 25);
        // outut field
        output.setBounds(45, 745, 630, 80);

        // Adds listener to connect button
        connectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String host = hostField.getText();
                String user = userField.getText();
                Integer port = Integer.parseInt(portField.getText());
                String password = String.valueOf(passwordField.getPassword());

                try {
                    ftpClient = Connection.Connect(host, port, user, password);
                    if (ftpClient == null) {
                        return;
                    } else {
                        connectButton.setEnabled(false);
                        disconnectButton.setEnabled(true);
                        ftpFileTree = new FTPFileTree(ftpClient);
                        serverTree.setModel(new DefaultTreeModel(ftpFileTree.getRoot()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // Adds Action listener for the disconnect button
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    ftpClient.disconnect();
                    setOutput("Disconnected from server.\n");
                    disconnectButton.setEnabled(false);
                    connectButton.setEnabled(true);
                    DefaultTreeModel model = (DefaultTreeModel) serverTree.getModel();
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                    root.removeAllChildren();
                    model.reload();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

        // *IMPORTANT* if I want to load the files recursively I need to create an
        // Expansion Listener

        // Adds a Selection Listener to the local tree
        localTree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent event) {

                localPath = event.getPath();

                DefaultMutableTreeNode localSelectedNode = (DefaultMutableTreeNode) localTree
                        .getLastSelectedPathComponent();
                if (localSelectedNode != null) {
                    if (localSelectedNode.isLeaf()) {
                        localFilePath = localPath;
                        localDirPath = null;
                    } else {
                        localDirPath = localPath;
                        localFilePath = null;
                    }
                }
            }

        });

        renameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                Object selectedObject = localTree.getLastSelectedPathComponent();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedObject;
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                TreeNode[] parentPath = parent.getPath();
                File fileToRename = new File(GetPath.getLocalFilePath(localPath));
                String path = GetPath.getParentPath(parentPath);
                File parentFile = new File(path);
                String name = JOptionPane
                        .showInputDialog("Insert new file name (don't forget to include file extension)", null);
                File newFileName = new File(path + File.separatorChar + name);
                if (fileToRename.renameTo(newFileName)) {
                    parent.removeAllChildren();
                    localFileTree.buildTree(parent, parentFile);
                    ((DefaultTreeModel) localTree.getModel()).reload(parent);
                    setOutput("File renamed successfully\n");
                } else {
                    setOutput("Failed to rename file\n");
                }
            }
        });

        localDeleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {

                if (localFilePath != null) {

                    Object selectedObject = localTree.getLastSelectedPathComponent();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedObject;
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                    TreeNode[] parentPath = parent.getPath();

                    String path = GetPath.getParentPath(parentPath);

                    File fileToDelete = new File(GetPath.getLocalFilePath(localPath));
                    File parentFile = new File(path);

                    int result = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete?");
                    if (result == JOptionPane.YES_OPTION) {
                        boolean deleteResult = fileToDelete.delete();
                        if (deleteResult) {
                            parent.removeAllChildren();
                            localFileTree.buildTree(parent, parentFile);
                            ((DefaultTreeModel) localTree.getModel()).reload(parent);
                            setOutput("File deleted!\n");
                        } else {
                            setOutput("Sorry, unable to delete file.\n");
                        }
                    }
                }
            }
        });

        /*****************************************************************/
        // Adds a Selection Listener to the server tree
        serverTree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent event) {
                TreePath serverPath = event.getPath();

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) serverTree
                        .getLastSelectedPathComponent();
                if (selectedNode != null) {
                    if (selectedNode.isLeaf()) {
                        serverFilePath = serverPath;
                        serverDirPath = null;
                    } else {
                        serverDirPath = serverPath;
                        serverFilePath = null;
                    }
                }
            }

        });

        // Adds an Action Listener to the upload button
        uploadButton.addActionListener(new ActionListener() {

            // Gets the paths of the file in the local tree and the path of the folder in
            // the server tree.
            // Saves the node selected, changes to the upload directory and after uploading
            // the file goes back to the parent directory.
            // Empties the node and load the complete list of files in the upload directory
            @Override
            public void actionPerformed(ActionEvent event) {
                if (localFilePath != null && serverDirPath != null) {

                    localPathS = GetPath.getLocalFilePath(localFilePath);
                    serverPathS = GetPath.getServerDirPath(serverDirPath);

                    Object selectedObject = serverTree.getLastSelectedPathComponent();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedObject;

                    // for regex to recongnize the '\' it need 4 of them
                    String[] arrOfString = localPathS.split("\\\\", 0);
                    String fileName = arrOfString[arrOfString.length - 1];
                    try {
                        ftpClient.changeWorkingDirectory(serverPathS);
                        SendFile.UploadFile(ftpClient, localPathS, fileName);

                        ftpClient.changeToParentDirectory();

                        node.removeAllChildren();
                        ftpFileTree.buildTree(node);

                        ((DefaultTreeModel) serverTree.getModel()).reload(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    setOutput("You need to select a path on the local tree and the server tree\n");
                }
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // necesito el directorio del servidor
                // el directorio donde voy a descargar el archivo
                // el nombre del archivo en el servidor

                if (localDirPath != null && serverFilePath != null) {
                    Object selectedObject = localTree.getLastSelectedPathComponent();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedObject;
                    TreeNode[] nodePath = node.getPath();
                    String path = GetPath.getParentPath(nodePath);
                    File nodeFile = new File(path);

                    localPathS = GetPath.getLocalFilePath(localDirPath);
                    serverPathS = GetPath.getServerDirPath(serverFilePath);

                    String[] arrOfString = serverPathS.split("/", 0);
                    String fileName = arrOfString[arrOfString.length - 1];

                    try {

                        // necesito sacar el nombre del archivo y reload el local file
                        FileOutputStream outputStream = new FileOutputStream(
                                localPathS + File.separatorChar + fileName);
                        ftpClient.retrieveFile(serverPathS, outputStream);
                        outputStream.close();
                        node.removeAllChildren();
                        localFileTree.buildTree(node, nodeFile);
                        ((DefaultTreeModel) localTree.getModel()).reload(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        newFolderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                if (serverDirPath != null) {
                    String name = JOptionPane.showInputDialog("Insert the folder name", null);
                    serverPathS = GetPath.getServerDirPath(serverDirPath);

                    Object selectedObject = serverTree.getLastSelectedPathComponent();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedObject;
                    try {
                        ftpClient.changeWorkingDirectory(serverPathS);
                        ftpClient.makeDirectory(name);
                        ftpClient.changeToParentDirectory();
                        node.removeAllChildren();
                        ftpFileTree.buildTree(node);
                        ((DefaultTreeModel) serverTree.getModel()).reload(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // deletes a file form the server
        // gets the node selected, moves to the parent node and after deleting the file
        // retrieves the new list of files
        serverDeleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                if (serverFilePath != null) {
                    Object selectedObject = serverTree.getLastSelectedPathComponent();
                    if (selectedObject instanceof DefaultMutableTreeNode) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedObject;
                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                        if (parent != null) {
                            serverPathS = GetPath.getServerDirPath(serverFilePath);
                            try {
                                ftpClient.deleteFile(serverPathS);
                                if (ftpClient.getReplyCode() == 250) {
                                    setOutput("File deleted\n");
                                    parent.removeAllChildren();
                                    ftpFileTree.buildTree(parent);
                                    ((DefaultTreeModel) serverTree.getModel()).reload(parent);
                                } else {
                                    setOutput("An error ocurred deleting the file\n");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        });

        // reload all the files from the server
        reloadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {

                try {
                    ftpClient.changeWorkingDirectory("/");
                    ftpFileTree = new FTPFileTree(ftpClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverTree.setModel(new DefaultTreeModel(ftpFileTree.getRoot()));

            }
        });

        // Set the frame properties
        setTitle("FTP Client");
        setSize(1030, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void setOutput(String detail) {
        output.append(detail);
    }

    public static void main(String[] args) throws IOException {
        new FTPClientGUI();
    }
}

/*
 * falta implementar la base de datos
 * falta implementar descargar
 */