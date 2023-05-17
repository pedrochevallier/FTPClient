package com.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.dao.DAOConnection;
import com.dao.DAOException;
import com.entity.Connection;

public class ConnectionDisplay extends JFrame {
    private JButton select;
    private JButton delete;
    private ArrayList<Connection> allConnections;
    private String[] connectionStrings;
    private DefaultListModel<String> listModel;

    public void showConnections() throws DAOException, IOException {

        select = new JButton("Select");
        delete = new JButton("Delete");

        final DAOConnection con = new DAOConnection();

        allConnections = con.searchAll();
        connectionStrings = arrToStr(allConnections);

        listModel = new DefaultListModel<String>();

        final JList<String> list = new JList<String>(listModel);

        // populate the list with the available connections
        for (String connection : connectionStrings) {
            listModel.addElement(connection);
        }

        JScrollPane scrollPane = new JScrollPane(list);

        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Connection selected = allConnections.get(selectedIndex);
                    FTPClientGUI.setHost(selected.getHost());
                    FTPClientGUI.setPort(selected.getPort());
                    FTPClientGUI.setUser(selected.getUserName());
                    FTPClientGUI.setPassword(selected.getPassword());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "No item selected");
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int id;
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Connection selected = allConnections.get(selectedIndex);
                    id = selected.getId();
                    int result = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete?");
                    if (result == JOptionPane.YES_OPTION) {
                        
                            try {
                                con.delete(id);
                                listModel.clear();
                                allConnections = con.searchAll();
                                connectionStrings = arrToStr(allConnections);
                                // populate the list with the available connections
                                for (String connection : connectionStrings) {
                                    listModel.addElement(connection);
                                }
                                list.repaint();
        
                            } catch (DAOException e) {
                                e.printStackTrace();
                            }
                    }
                    

                } else {
                    JOptionPane.showMessageDialog(null, "No item selected");
                }
            }
        });

        setLayout(null);

        add(scrollPane);
        add(select);
        add(delete);

        scrollPane.setBounds(50, 50, 400, 100);
        select.setBounds(200, 175, 100, 25);
        delete.setBounds(200, 215, 100, 25);

        setTitle("Connections");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private String[] arrToStr(ArrayList<Connection> con) {
        String[] connectionStrings = new String[con.size()];

        // transform ArrayList to String[]
        for (int i = 0; i < con.size(); i++) {
            Connection connection = con.get(i);
            connectionStrings[i] =  " Host: " + connection.getHost() + " | Port: "
                    + connection.getPort() + " | User: " + connection.getUserName() + " | ******";
        }
        return connectionStrings;
    }
}
