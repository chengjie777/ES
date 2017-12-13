package cn.elasticsearch.dao.impl;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.elasticsearch.dao.BaseDao;


/**
 * 1. 子类只要添加注解，父类其他注解仍生效
 * 	@Repository
 *  public class SysUserDaoImpl
 * 2. 父类Base 如果添加@Autowired 仍可以进行注入
 * 	@Autowired
	public void setSF(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
 * 3. 给BaseDao注入了SessionFactory，其实调用HibernateDaoSupport的setter方法
 * 	底层直接创建模板 ， new HibernateTemplate(sessionFactory);
 *   总结：spring创建SessionFactory，HibernateDaosupport创建模板 (使用默认值)
 * 
 * 4.如果spring创建模板，注入时，不注入SessionFactory，而是模板(参数自己设置)
 * 
 */
public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {
	
	//注入模板  -- framework/spring/applicationContext.xml 进行配置
	@Autowired
	public void setHT(HibernateTemplate hibernateTemplate){
		super.setHibernateTemplate(hibernateTemplate);
	}

	private Class<T> baseClass;
	
	public BaseDaoImpl() {
		try {
			//1 this 当前运行类，及new对象 *Dao
			Type type = this.getClass().getGenericSuperclass();
			//2 判断
			if(type instanceof ParameterizedType){
				ParameterizedType paramType = (ParameterizedType) type;
				
				//3 获得第一个实际参数
				baseClass = (Class<T>) paramType.getActualTypeArguments()[0];
			}
			
			//3 处理
			if(baseClass == null){
				throw new RuntimeException(this + "不能获得父类泛型信息");
			}
			
		} catch (Exception e) {
			//将所有异常转换成自定义异常，暂时采购运行时异常
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void save(T t) {
		this.getHibernateTemplate().save(t);
	}

	@Override
	public void update(T t) {
		this.getHibernateTemplate().update(t);
	}

	@Override
	public void delete(T t) {
		this.getHibernateTemplate().delete(t);
	}

	@Override
	public T findById(Serializable id) {
//		return this.getHibernateTemplate().get(T.class, id);
		return this.getHibernateTemplate().get(baseClass, id);
	}

	@Override
	public List<T> findAll() {
//		return (List<T>) this.getHibernateTemplate().find("from T");
//		return (List<T>) this.getHibernateTemplate().find("from SysUser");			//简化写法，自动添加包 <hibernate-mapping auto-import="true">
//		return (List<T>) this.getHibernateTemplate().find("from cn.itcast.yycg.domain.po.SysUser");	//标准写法
		return (List<T>) this.getHibernateTemplate().find("from " + baseClass.getName());
	}

}
