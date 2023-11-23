package com.soho.pos.service;

import com.soho.pos.dao.BaseDAO;
import com.soho.pos.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseService<T extends BaseEntity, ID extends Serializable, R extends BaseDAO<T, ID>> {
    @PersistenceContext
    protected EntityManager em;
    @Autowired
    protected R dao;


    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void delete(ID id) {
        dao.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void delete(Iterable<ID> ids) {
        for (ID id : ids) {
            dao.deleteById(id);
        }
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void clear() {
        dao.deleteAllInBatch();
    }


    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public T insert(T entity) {
        return (T) dao.insert(entity);
    }


    public T get(ID id) {
        Optional<T> result = dao.findById(id);
        return result.orElse(null);
    }


    public List<T> getList(Iterable<ID> ids) {
        return dao.findAllById(ids);
    }


    public List<T> getAll() {
        return dao.findAll();
    }


    public List<T> getAll(String order, String column) {
        Sort.Direction dataOrder = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return dao.findAll(Sort.by(dataOrder, column));
    }


    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public T update(T entity) {
        return (T) dao.update(entity);
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void delete(T entity) {
        dao.delete(entity);
    }

}
