package com.soho.pos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDAO<T, ID extends Serializable> extends JpaRepository<T, ID> {

    T insert(T entity);

    T update(T entity);

    void delete(T entity);

    int findMaxSeq();

}
