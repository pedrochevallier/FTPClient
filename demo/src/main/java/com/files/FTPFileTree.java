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

    // crea el arbol de archivos de forma recursiva
    public void buildTree(DefaultMutableTreeNode node) throws IOException {

        String path = getPath(node);
        FTPFile[] files = ftpClient.listFiles(path);

        if (files != null) {
            for (FTPFile file : files) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getName());
                node.add(childNode);

                if (file.isDirectory()) {
                    buildTree(childNode);

                    // puedo usar buildTree para hacer el arbol de forma recursiva
                    // o usar un actionListener para hacerlo solo cuando el usuario
                    // abre una carpeta con la desventaja que crea un nodo vacio

                    //childNode.add(new DefaultMutableTreeNode());
                }
            }
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
/* 
    public void loadChildren(DefaultMutableTreeNode node) throws IOException {
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
    } */

    // devuelve el nodo raiz con todos los nodos hijo
    public DefaultMutableTreeNode getRoot() {
        return root;
    }
}
