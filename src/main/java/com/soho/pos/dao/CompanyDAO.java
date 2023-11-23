package com.soho.pos.dao;

import com.soho.pos.entity.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDAO extends BaseDAO<Company, Long> {
    
    Company findByBrand(String brand);
    
}

