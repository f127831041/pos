package com.soho.pos.dao;

import com.soho.pos.entity.Purchase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PurchaseDAO extends BaseDAO<Purchase, Long> {
    
    Purchase findByProductIdAndSizeId(long productId, long sizeId);
}

