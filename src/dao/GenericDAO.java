package dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T, ID> {
    public void add(T obj) throws SQLException, IllegalArgumentException;
    public T findByID(ID id) throws SQLException, IllegalArgumentException;
    public void delete(T obj) throws SQLException, IllegalArgumentException;
    public void update(T obj) throws SQLException, IllegalArgumentException;
    public List<T> getAll() throws SQLException;
}

