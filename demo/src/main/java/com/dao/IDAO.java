package com.dao;

import java.util.ArrayList;

import com.entity.Connection;

public interface IDAO<T> {
    public void save(T element) throws DAOException;
    public void delete(int id) throws DAOException;
    public ArrayList<Connection> searchAll() throws DAOException;
}
