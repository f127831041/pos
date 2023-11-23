package com.soho.pos.dao;

import com.soho.pos.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerDAO  extends BaseDAO<Manager, Long> {
    Manager findByAccountAndPassword(String account, String password);

    Manager findByAccount(String account);
    
}

