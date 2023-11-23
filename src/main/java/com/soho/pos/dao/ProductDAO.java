package com.soho.pos.dao;

import com.soho.pos.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductDAO extends BaseDAO<Product, Long> {
    
    @Transactional(readOnly = true, timeout = 30)
    @Query(value = "SELECT p FROM Product p WHERE p.companyId = ?1 AND p.status = '1' order by p.id desc ")
    List<Product> findByCompanyId(long companyId);
    
    List<Product> findByProdNo(String prodNo);
    
    List<Product> findByDesign(String design);
}

