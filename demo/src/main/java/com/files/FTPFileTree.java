package com.files;

import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPFileTree {
    private DefaultMutableTreeNode root;
    private FTPClient ftpClient;

    public FTPFileTree(String server, int port, String username, String password, int maxDepth) throws IOException {
        root = new DefaultMutableTreeNode(server);

        DefaultTreeModel model = new DefaultTreeModel(root);

        ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(username, password);

        buildTree(root, 0, maxDepth);

        /* ftpClient.logout();
        ftpClient.disconnect(); */
    }

    private void buildTree(DefaultMutableTreeNode node, int depth, int maxDepth) throws IOException {
        if (depth >= maxDepth) {
            return;
        }

        String path = getPath(node);
        FTPFile[] files = ftpClient.listFiles(path);

        if (files != null) {
            for (FTPFile file : files) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getName());
                node.add(childNode);

                if (file.isDirectory()) {
                    childNode.add(new DefaultMutableTreeNode());
                }
            }
        }
    }

    private String getPath(DefaultMutableTreeNode node) {
        StringBuilder path = new StringBuilder();
        Enumeration<TreeNode> enumeration = node.pathFromAncestorEnumeration(root);
        while (enumeration.hasMoreElements()) {
            TreeNode ancestor = enumeration.nextElement();
            if (((DefaultMutableTreeNode) ancestor).isRoot()) {
                path.append("/");
            } else {
                path.append(ancestor.toString());
                path.append("/");
            }
        }
        return path.toString();
    }

    private boolean isDirectory(String name) throws IOException {
        boolean isDirectory = false;
        String originalWorkingDirectory = ftpClient.printWorkingDirectory();
        String path = getPath(new DefaultMutableTreeNode(name));
    
        if (ftpClient.changeWorkingDirectory(path)) {
            isDirectory = true;
            ftpClient.changeWorkingDirectory(originalWorkingDirectory);
        }
    
        return isDirectory;
    }
    public void loadChildren(DefaultMutableTreeNode node, int maxDepth) throws IOException {
        Enumeration enumeration = node.children();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enumeration.nextElement();
            if (childNode.getChildCount() == 0) {
                String path = getPath(childNode);
                FTPFile[] files = ftpClient.listFiles(path);

                if (files != null) {
                    for (FTPFile file : files) {
                        DefaultMutableTreeNode grandChildNode = new DefaultMutableTreeNode(file.getName());
                        childNode.add(grandChildNode);

                        if (file.isDirectory()) {
                            if (maxDepth > 1) {
                                grandChildNode.add(new DefaultMutableTreeNode());
                            }
                        }
                    }
                }
            }
        }
    }


    public DefaultMutableTreeNode getRoot() {
        return root;
    }
}
