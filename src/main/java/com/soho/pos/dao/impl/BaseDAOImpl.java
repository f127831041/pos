package com.soho.pos.dao.impl;

import com.soho.pos.dao.BaseDAO;
import com.soho.pos.entity.BaseEntity;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;

@Transactional
public class BaseDAOImpl<T extends BaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDAO<T, ID> {

    private final EntityManager em;
    private Class<T> entityClass;


    public BaseDAOImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
        this.entityClass = entityInformation.getJavaType();
    }


    public BaseDAOImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.em = entityManager;
    }

    @Override
    public void delete(T entity) {
        em.remove(em.merge(entity));
        em.flush();
        em.clear();
    }

    @Override
    public T insert(T entity) {
        em.persist(entity);
        em.flush();
        em.clear();
        return entity;
    }

    @Override
    public T update(T entity) {
        em.merge(entity);
        em.flush();
        em.clear();
        return entity;
    }

    public int findMaxSeq() {
        StringBuilder sql = new StringBuilder();
        String tableName = entityClass.getAnnotation(javax.persistence.Table.class).name();
        sql.append("SELECT IFNULL(max(seq)+1, 1) as seq FROM ").append(tableName);
        Query query = em.createNativeQuery(sql.toString());
        return ((Number) query.getSingleResult()).intValue();
    }

}
