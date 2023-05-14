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

    public FTPFileTree(FTPClient client) throws IOException {
        root = new DefaultMutableTreeNode("/");
        ftpClient = client;

        System.out.println("Getting server files...");
        buildTree(root);

    }

    // crea el arbol de archivos de forma recursiva
    public void buildTree(DefaultMutableTreeNode node) throws IOException {
        String path = getPath(node);
        FTPFile[] files = ftpClient.listFiles(path);
        System.out.println(path + " has " + files.length + " elements");

        if (files.length > 0) {
            for (FTPFile file : files) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getName());
                node.add(childNode);

                if (file.isDirectory()) {
                    buildTree(childNode);
                }
            }
        }
        else{
            DefaultMutableTreeNode child = new DefaultMutableTreeNode("Empty folder");
            node.add(child);
            }
    }

    // devuelve el path del nodo
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

    // devuelve el nodo raiz con todos los nodos hijo
    public DefaultMutableTreeNode getRoot() {
        return root;
    }
}
