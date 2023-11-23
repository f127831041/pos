package com.soho.pos.dao;

import com.soho.pos.entity.Returned;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnedDAO extends BaseDAO<Returned, Long> {
    
    Returned findByProductIdAndSizeId(long productId, long sizeId);
}

