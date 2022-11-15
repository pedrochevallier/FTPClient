package com.clientesftps.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.*;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;

public class LocalFS extends JPanel{

    protected static FileSystemView fsv = FileSystemView.getFileSystemView(); //API to file system
    private JTree tree;
    

    // le pone valores a los tree cells
    private static class FileTreeCellRenderer extends DefaultTreeCellRenderer {

		private Map<String, Icon> iconCache = new HashMap<String, Icon>();
		private Map<File, String> rootNameCache = new HashMap<File, String>();

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			FileTreeNode ftn = (FileTreeNode) value;
			File file = ftn.file;
			String filename = "";
			if (file != null) {
				if (ftn.isFileSystemRoot) {
					filename = this.rootNameCache.get(file);
					if (filename == null) {
						filename = fsv.getSystemDisplayName(file);
						this.rootNameCache.put(file, filename);
					}
				} else {
					filename = file.getName();
				}
			}
			JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
					filename, sel, expanded, leaf, row, hasFocus);
			if (file != null) {
				Icon icon = this.iconCache.get(filename);
				if (icon == null) {
					icon = fsv.getSystemIcon(file);
					this.iconCache.put(filename, icon);
				}
				result.setIcon(icon);
			}
			return result;
		}
	}


	private static class FileTreeNode implements TreeNode {

		private File file;
		private File[] children;
		private TreeNode parent;
		private boolean isFileSystemRoot;

		public FileTreeNode(File file, boolean isFileSystemRoot, TreeNode parent) {
			this.file = file;
			this.isFileSystemRoot = isFileSystemRoot;
			this.parent = parent;
			this.children = this.file.listFiles();
			if (this.children == null)
				this.children = new File[0];
		}

		public FileTreeNode(File[] children) {
			this.file = null;
			this.parent = null;
			this.children = children;
		}

		public Enumeration children() {
			final int elementCount = this.children.length;
			return new Enumeration<File>() {
				int count = 0;

				public boolean hasMoreElements() {
					return this.count < elementCount;
				}

				public File nextElement() {
					if (this.count < elementCount) {
						return FileTreeNode.this.children[this.count++];
					}
					throw new NoSuchElementException("Vector Enumeration");
				}
			};
		}
		
		public boolean getAllowsChildren() {
			return true;
		}

		public TreeNode getChildAt(int childIndex) {
			return new FileTreeNode(this.children[childIndex],
					this.parent == null, this);
		}

		public int getChildCount() {
			return this.children.length;
		}
	
		public int getIndex(TreeNode node) {
			FileTreeNode ftn = (FileTreeNode) node;
			for (int i = 0; i < this.children.length; i++) {
				if (ftn.file.equals(this.children[i]))
					return i;
			}
			return -1;
		}

		public TreeNode getParent() {
			return this.parent;
		}
		
        public boolean isLeaf() {
			return (this.getChildCount() == 0);
		}
	}


	public LocalFS() {
		this.setLayout(new BorderLayout());

		File[] roots = File.listRoots();
		FileTreeNode rootTreeNode = new FileTreeNode(roots);
		this.tree = new JTree(rootTreeNode);
		this.tree.setCellRenderer(new FileTreeCellRenderer()); //le pone los nombres a los nodos
		this.tree.setRootVisible(false);
		final JScrollPane jsp = new JScrollPane(this.tree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}
