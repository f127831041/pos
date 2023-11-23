package com.soho.pos.service;

import com.soho.pos.dao.ManagerDAO;
import com.soho.pos.dto.CompanyDTO;
import com.soho.pos.dto.ManagerDTO;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Manager;
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
public class ManagerService extends BaseService<Manager, Long, ManagerDAO> {
    
    public PageResult findPageQuery(ManagerDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Manager where 1 = 1 ");
        
        if (StringUtils.isNotBlank(rq.getAccount())) {
            sql.append(" and account like :account ");
            params.put("account", "%" + rq.getAccount() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getCname())) {
            sql.append(" and cname like :cname ");
            params.put("cname", "%" + rq.getCname() + "%");
        }
    
        if (StringUtils.isNotBlank(rq.getStatus())) {
            sql.append(" and status = :status ");
            params.put("status", rq.getStatus());
        }
        
        if (StringUtils.isNotBlank(rq.getPhone())) {
            sql.append(" and phone like :phone ");
            params.put("phone", "%" + rq.getPhone() + "%");
        }
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Manager> resultList = query.getResultList();
        return PageResult.getPageResult(resultList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void add(ManagerDTO rq) throws PortalException {
        Manager manager = new Manager();
        manager.setAccount(rq.getAccount());
        manager.setPassword(rq.getPassword());
        manager.setCname(rq.getCname());
        manager.setStatus(rq.getStatus());
        manager.setPhone(rq.getPhone());
        dao.insert(manager);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(ManagerDTO rq) throws PortalException {
        Manager manager = dao.findById(rq.getId()).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        manager.setAccount(rq.getAccount());
        manager.setCname(rq.getCname());
        manager.setStatus(rq.getStatus());
        manager.setPhone(rq.getPhone());
        if(StringUtils.isNotBlank(rq.getPassword())){
            manager.setPassword(rq.getPassword());
        }
        dao.update(manager);
    }
}
