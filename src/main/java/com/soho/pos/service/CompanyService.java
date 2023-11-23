package com.soho.pos.service;

import com.soho.pos.dao.CompanyDAO;
import com.soho.pos.dto.CompanyDTO;
import com.soho.pos.entity.Company;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService extends BaseService<Company, Long, CompanyDAO> {
    
    public PageResult findPageQuery(CompanyDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Company where 1 = 1 ");
        
        if (StringUtils.isNotBlank(rq.getBrand())) {
            sql.append(" and brand like :brand ");
            params.put("brand", "%" + rq.getBrand() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getSalesName())) {
            sql.append(" and (salesName1 like :salesName or salesName2 like :salesName) ");
            params.put("salesName", "%" + rq.getSalesName() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getMobilePhone())) {
            sql.append(" and (mobilePhone1 like :mobilePhone or mobilePhone2 like :mobilePhone) ");
            params.put("mobilePhone", "%" + rq.getMobilePhone() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getPhone())) {
            sql.append(" and (phone1 like :phone or phone2 like :phone) ");
            params.put("phone", "%" + rq.getPhone() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getAddress())) {
            sql.append(" and (address1 like :address or address2 like :address) ");
            params.put("address", "%" + rq.getAddress() + "%");
        }
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Company> resultList = query.getResultList();
        return PageResult.getPageResult(resultList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void add(CompanyDTO rq) throws PortalException {
        Company company = dao.findByBrand(rq.getBrand());
        if (company != null) {
            throw new PortalException(ErrorText.COMPANY_EXISTS_FAIL.getMsg());
        }
        company = new Company();
        company.setBrand(rq.getBrand());
        company.setMobilePhone1(rq.getMobilePhone());
        company.setMobilePhone2(rq.getMobilePhone2());
        company.setPhone1(rq.getPhone());
        company.setPhone2(rq.getPhone2());
        company.setAddress1(rq.getAddress());
        company.setAddress2(rq.getAddress2());
        company.setSalesName1(rq.getSalesName());
        company.setSalesName2(rq.getSalesName2());
        dao.insert(company);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(CompanyDTO rq) throws PortalException {
        Company company = dao.findById(rq.getId()).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        Company dbCompany = dao.findByBrand(rq.getBrand());
        if(dbCompany != null){
            if(company.getId().longValue() != dbCompany.getId().longValue()){
                throw new PortalException(ErrorText.COMPANY_EXISTS_FAIL.getMsg());
            }
        }
        company.setBrand(rq.getBrand());
        company.setMobilePhone1(rq.getMobilePhone());
        company.setMobilePhone2(rq.getMobilePhone2());
        company.setPhone1(rq.getPhone());
        company.setPhone2(rq.getPhone2());
        company.setAddress1(rq.getAddress());
        company.setAddress2(rq.getAddress2());
        company.setSalesName1(rq.getSalesName());
        company.setSalesName2(rq.getSalesName2());
        dao.update(company);
    }
}
