package cn.elasticsearch.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {
	public void save(T t);
	public void update(T t);
	public void delete(T t);
	
	public T findById(Serializable id);
	
	public List<T> findAll();
}
