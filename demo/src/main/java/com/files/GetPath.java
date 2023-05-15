package com.files;

import java.io.File;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class GetPath {

    public static String getLocalFilePath(TreePath localTreePath) {
        String homeDirectory = System.getProperty("user.home");

        StringBuilder sb = new StringBuilder();
        Object[] nodes = localTreePath.getPath();

        sb.append(homeDirectory);
        for (int i = 0; i < nodes.length; i++) {
            sb.append(File.separatorChar).append(nodes[i].toString());
        }
        return sb.toString();
    }

    public static String getServerDirPath(TreePath serverTreePath) {
        StringBuilder sb = new StringBuilder();
        Object[] nodes = serverTreePath.getPath();
        if(nodes.length ==1){
            sb.append("/");
        }
        for (int i = 1; i < nodes.length; i++) {
            sb.append("/").append(nodes[i].toString());
        }
        return sb.toString();
    }

    public static String getParentPath(TreeNode[] parentTreePath){
        String homeDirectory = System.getProperty("user.home");
                    
                    StringBuilder sb = new StringBuilder();
                    sb.append(homeDirectory);

                    for (TreeNode pathNode : parentTreePath) {
                        sb.append(File.separatorChar).append(pathNode.toString());
                    }
        return sb.toString(); 
    }
}
