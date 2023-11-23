package com.soho.pos.dao;

import com.soho.pos.entity.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InventoryDAO extends BaseDAO<Inventory, Long> {
    
    @Transactional(readOnly = true, timeout = 30)
    @Query(value = "SELECT i FROM Inventory i WHERE i.productId = ?1 ORDER BY i.id desc")
    List<Inventory> findByProductId(long id);
    
    Inventory findByProductIdAndSizeId(long productId, long sizeId);
}

