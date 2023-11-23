package com.soho.pos.dao;

import com.soho.pos.entity.ReturnedHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReturnedHistoryDAO extends BaseDAO<ReturnedHistory, Long> {
    @Transactional(readOnly = true, timeout = 30)
    @Query(value = "SELECT distinct(r.buyNo) FROM ReturnedHistory r WHERE r.buyNo like ?1%")
    List<String> findBuyNoDistinct(String buyNo);
    
    List<ReturnedHistory> findByBuyNo(String buyNo);
    
    @Transactional(readOnly = true, timeout = 30)
    @Query(value = "SELECT ifnull(max(r.buy_no),'order00000000') FROM returned_history r", nativeQuery = true)
    String findMaxNo();
    
}

