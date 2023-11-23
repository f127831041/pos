package com.soho.pos.dao;

import com.soho.pos.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PurchaseHistoryDAO extends BaseDAO<PurchaseHistory, Long> {
    @Transactional(readOnly = true, timeout = 30)
    @Query(value = "SELECT distinct(p.buyNo) FROM PurchaseHistory p WHERE p.buyNo like ?1%")
    List<String> findBuyNoDistinct(String buyNo);
    
    List<PurchaseHistory> findByBuyNo(String buyNo);
    
    @Transactional(readOnly = true, timeout = 30)
    @Query(value = "SELECT ifnull(max(p.buy_no),'order00000000') FROM purchase_history p", nativeQuery = true)
    String findMaxNo();
    
}

