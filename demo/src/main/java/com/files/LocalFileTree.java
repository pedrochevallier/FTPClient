package com.files;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;

public class LocalFileTree {
    private DefaultMutableTreeNode root;

    // Creates the root node with the user home directory
    public LocalFileTree() {
        File homeDirectory = new File(System.getProperty("user.home") + File.separator + "Documents");
        root = new DefaultMutableTreeNode(homeDirectory.getName());
        buildTree(root, homeDirectory);
    }

    // receives the root node and gets all children nodes
    public void buildTree(DefaultMutableTreeNode node, File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
    
            if (files != null && files.length > 0) {
                for (File child : files) {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child.getName());
                    node.add(childNode);
                    if (child.isDirectory()) {
                        buildTree(childNode, child);
                    }
                }
            }else {
                // Add a child node with the directory name to represent an empty directory
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
                node.add(childNode);
            }
        } 
    }

    // Returns the root node with all children
    public DefaultMutableTreeNode getRoot() {
        return root;
    }
}

