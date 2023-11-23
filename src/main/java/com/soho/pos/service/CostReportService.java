package com.soho.pos.service;

import com.soho.pos.dao.CompanyDAO;
import com.soho.pos.dao.ProductDAO;
import com.soho.pos.dao.SaleDAO;
import com.soho.pos.dto.CompanyReportDTO;
import com.soho.pos.dto.CostReportDTO;
import com.soho.pos.entity.*;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.CompanyReportVO;
import com.soho.pos.vo.CostReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CostReportService {
    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private CompanyDAO companyDAO;
    
    
    public PageResult findPageQuery(CostReportDTO rq) {
        List<PurchaseHistory> purchaseList = getPurchaseList(rq.getDate(), rq.getCompanyId());
        List<ReturnedHistory> returnedList = getReturnedList(rq.getDate(), rq.getCompanyId());
        List<Long> companyIds = new ArrayList<>();
        
        //進貨+總
        Map<Long, Integer> purchaseMap = new HashMap<>();
        for (PurchaseHistory history : purchaseList) {
            int total = purchaseMap.getOrDefault(history.getCompanyId(), 0);
            int tariff = history.getTariff();
            int sum = history.getCnt() * history.getPrice();
            int tariffPay = (int) Math.floor(sum * (0.01 * (tariff)));
            total += sum + tariffPay;
            purchaseMap.put(history.getCompanyId(), total);
            if (!companyIds.contains(history.getCompanyId())) {
                companyIds.add(history.getCompanyId());
            }
        }
        //退貨+總
        Map<Long, Integer> returnedMap = new HashMap<>();
        for (ReturnedHistory history : returnedList) {
            int total = returnedMap.getOrDefault(history.getCompanyId(), 0);
            int tariff = history.getTariff();
            int sum = history.getCnt() * history.getPrice();
            int tariffPay = (int) Math.floor(sum * (0.01 * (tariff)));
            total += sum + tariffPay;
            returnedMap.put(history.getCompanyId(), total);
            if (!companyIds.contains(history.getCompanyId())) {
                companyIds.add(history.getCompanyId());
            }
        }
        
        Map<Long, Company> companyMap = companyDAO.findAll().stream().collect(Collectors.toMap(Company::getId, Function.identity()));
        List<CostReportVO> voList = new ArrayList<>();
        for (long id : companyIds) {
            Company company = companyMap.get(id);
            int purchase = purchaseMap.getOrDefault(id, 0);
            int returned = returnedMap.getOrDefault(id, 0);
            CostReportVO vo = new CostReportVO();
            vo.setCompany(company);
            vo.setPurchase(purchase);
            vo.setReturned(returned);
            vo.setTotal(purchase - returned);
            voList.add(vo);
        }
        voList = voList.stream().sorted(Comparator.comparing(CostReportVO::getTotal).reversed()).collect(Collectors.toList());
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    public List<PurchaseHistory> getPurchaseList(String date, Long companyId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from PurchaseHistory where 1 = 1 ");
        
        if (StringUtils.isNotBlank(date)) {
            String sDate = date.split("至")[0].trim();
            String eDate = date.split("至")[1].trim();
            sql.append(" and purchaseDate >= :sDate ");
            params.put("sDate", LocalDate.parse(sDate));
            
            sql.append(" and purchaseDate <= :eDate ");
            params.put("eDate", LocalDate.parse(eDate));
        }
        
        if (companyId != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", companyId);
        }
        sql.append(" order by companyId desc");
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        return (List<PurchaseHistory>) query.getResultList();
    }
    
    public List<ReturnedHistory> getReturnedList(String date, Long companyId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from ReturnedHistory where 1 = 1 ");
        
        if (StringUtils.isNotBlank(date)) {
            String sDate = date.split("至")[0].trim();
            String eDate = date.split("至")[1].trim();
            sql.append(" and returnedDate >= :sDate ");
            params.put("sDate", LocalDate.parse(sDate));
            
            sql.append(" and returnedDate <= :eDate ");
            params.put("eDate", LocalDate.parse(eDate));
        }
        
        if (companyId != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", companyId);
        }
        sql.append(" order by companyId desc");
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        return (List<ReturnedHistory>) query.getResultList();
    }
}
