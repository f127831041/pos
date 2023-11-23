package com.soho.pos.service;

import com.soho.pos.dao.CompanyDAO;
import com.soho.pos.entity.Company;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.Chart1VO;
import com.soho.pos.vo.Chart2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HomeService {
    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private CompanyDAO companyDAO;
    
    public Map<String, Object> getCompanyChartData() {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        LocalDate firstDay = LocalDate.now().minusDays(6);
        ;
        LocalDate lastDay = LocalDate.now();
        
        sql.append(" SELECT company_id, SUBSTR(create_date_time,1,10), SUM(cnt) ");
        sql.append(" FROM product_rank where 1 = 1 ");
        sql.append(" and substr(create_date_time, 1, 10) >= :firstDay ");
        params.put("firstDay", firstDay);
        
        sql.append(" and substr(create_date_time, 1, 10) <= :lastDay ");
        params.put("lastDay", lastDay);
        
        sql.append(" group by company_id, substr(create_date_time,1,10) ");
        sql.append(" order by company_id desc");
        
        Query query = em.createNativeQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Object[]> resultList = query.getResultList();
        
        List<LocalDate> dateRange = new ArrayList<>();
        LocalDate currentDate = firstDay;
        
        while (!currentDate.isAfter(lastDay)) {
            dateRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        List<String> dateList = dateRange.stream().map(LocalDate::toString).collect(Collectors.toList());
        List<Company> companyList = companyDAO.findAll().stream().sorted(Comparator.comparing(Company::getId).reversed()).collect(Collectors.toList());
        List<Chart1VO> chart1List = new ArrayList<>();
        for (Object[] result : resultList) {
            Chart1VO chart1 = new Chart1VO();
            chart1.setCompanyId(Long.parseLong(result[0].toString()));
            chart1.setDate((result[1]).toString());
            chart1.setCnt(Integer.parseInt(result[2].toString()));
            chart1List.add(chart1);
        }
        Map<Long, List<Chart1VO>> dataMap = chart1List.stream().collect(Collectors.groupingBy(Chart1VO::getCompanyId));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("dateList", dateList);
        resultMap.put("companyList", companyList);
        resultMap.put("dataMap", dataMap);
        return resultMap;
    }
    
    public Map<String, Object> getRevenueChartData() {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        LocalDate firstDay = LocalDate.now().minusDays(6);
        ;
        LocalDate lastDay = LocalDate.now();
        
        sql.append(" SELECT SUBSTR(create_date_time,1,10), SUM(pay) ");
        sql.append(" FROM revenue where 1 = 1 ");
        sql.append(" and substr(create_date_time, 1, 10) >= :firstDay ");
        params.put("firstDay", firstDay);
        
        sql.append(" and substr(create_date_time, 1, 10) <= :lastDay ");
        params.put("lastDay", lastDay);
        
        sql.append(" group by substr(create_date_time,1,10) ");
        sql.append(" order by create_date_time asc");
        
        Query query = em.createNativeQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Object[]> resultList = query.getResultList();
        
        List<LocalDate> dateRange = new ArrayList<>();
        LocalDate currentDate = firstDay;
        
        while (!currentDate.isAfter(lastDay)) {
            dateRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        List<String> dateList = dateRange.stream().map(LocalDate::toString).collect(Collectors.toList());
        
        List<Chart2VO> chart2List = new ArrayList<>();
        for (Object[] result : resultList) {
            Chart2VO chart2 = new Chart2VO();
            chart2.setDate((result[0]).toString());
            chart2.setPay(Integer.parseInt(result[1].toString()));
            chart2List.add(chart2);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("dateList", dateList);
        resultMap.put("dataList", chart2List);
        return resultMap;
    }
}
