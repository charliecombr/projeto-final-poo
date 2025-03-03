package dao;

import java.util.List;

public interface GenericDAO <T, ID>{

	public void add(T obj);
	public T findByID(ID id);
	public void delete(T obj);
	public void update(T obj);
	public List<T> getAll();
}
