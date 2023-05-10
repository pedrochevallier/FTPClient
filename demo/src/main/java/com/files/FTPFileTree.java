package com.files;

import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPFileTree {
    private DefaultMutableTreeNode root;
    private FTPClient ftpClient;

    public FTPFileTree(String server, FTPClient client, int maxDepth) throws IOException {
        root = new DefaultMutableTreeNode(server);
        ftpClient = client;

        System.out.println("Getting server files...");
        buildTree(root);

    }

    private void buildTree(DefaultMutableTreeNode node) throws IOException {

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
                
            } else {
                path.append(ancestor.toString());
                path.append("/");

            }
        }
        return path.toString();
    }

    public void loadChildren(DefaultMutableTreeNode node) throws IOException {
        System.out.println("expanding node");
        Enumeration enumeration = node.children();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enumeration.nextElement();
            if (childNode.getChildCount() == 0) {
                String path = getPath(childNode);
                System.out.println(path);
                FTPFile[] files = ftpClient.listFiles(path);

                if (files != null) {
                    for (FTPFile file : files) {
                        DefaultMutableTreeNode grandChildNode;
                        if (file.isDirectory()) {
                            grandChildNode = new DefaultMutableTreeNode(file.getName());
                            childNode.add(grandChildNode);
                        } else {
                            grandChildNode = new DefaultMutableTreeNode(file.getName());
                        }
                        
                    }
                }
            }
        }
    }

    public DefaultMutableTreeNode getRoot() {
        System.out.println("Returning root");
        System.out.println(root);
        return root;
    }
}
