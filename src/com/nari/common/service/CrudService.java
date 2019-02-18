package com.nari.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.nari.common.persistence.BaseEntity;
import com.nari.common.persistence.CrudDao;


@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends BaseEntity<T>> extends BaseService {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> search(T entity) {
		return dao.search(entity);
	}	
	
	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int save(T entity) {
		
		if (entity.getIsNewRecord()){
			//entity.preInsert();
			return dao.insert(entity);
		}else{
			//entity.preUpdate();
			return dao.update(entity);
		}
	}	
	
	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int delete(T entity) {
		return dao.delete(entity);
	}	
}
