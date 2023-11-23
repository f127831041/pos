package com.soho.pos.dao;

import com.soho.pos.entity.ProductRank;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface ProductRankDAO extends BaseDAO<ProductRank, Long> {
    
    @Transactional(readOnly = true, timeout = 30)
    @Query(value = "SELECT r FROM ProductRank r WHERE r.productId = ?1 AND r.saleDate = ?2")
    ProductRank findByProductId(long productId, LocalDate date);
}

