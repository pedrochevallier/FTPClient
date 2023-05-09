package com.files;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class LocalFileTree {
    private DefaultMutableTreeNode root;

    public LocalFileTree() {
        root = new DefaultMutableTreeNode(new File(System.getProperty("user.home") + "/Documents"));

        DefaultTreeModel model = new DefaultTreeModel(root);

        File[] roots = File.listRoots();
        for (File file : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
            root.add(node);
        }

        buildTree(root);
    }

    private void buildTree(DefaultMutableTreeNode node) {
        File file = (File) node.getUserObject();

        if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (File child : files) {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                    node.add(childNode);
                    buildTree(childNode);
                }
            }
        }
    }

    public DefaultMutableTreeNode getRoot() {
        return root;
    }
}

