package com.soho.pos.service;

import com.soho.pos.dao.MemberDAO;
import com.soho.pos.dao.RevenueDAO;
import com.soho.pos.dto.RevenueDTO;
import com.soho.pos.entity.Member;
import com.soho.pos.entity.Revenue;
import com.soho.pos.mapper.RevenueMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.RevenueVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RevenueService extends BaseService<Revenue, Long, RevenueDAO> {
    @Autowired
    private MemberDAO memberDAO;
    
    public PageResult findPageQuery(RevenueDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append("from Revenue where 1 = 1 ");
        
        if (StringUtils.isNotBlank(rq.getDate())) {
            String sDate = rq.getDate().split("至")[0].trim();
            String eDate = rq.getDate().split("至")[1].trim();
            sql.append(" and substr(createDateTime, 1, 10) >= :sDate ");
            params.put("sDate", sDate);
            
            sql.append(" and substr(createDateTime, 1, 10) <= :eDate ");
            params.put("eDate", eDate);
        }
    
        if(StringUtils.isNotBlank(rq.getPhone())){
            Member member = memberDAO.findByPhone(rq.getPhone());
            long id = member == null ? -1 : member.getId();
            sql.append(" and memberId = :memberId ");
            params.put("memberId", id);
        }
    
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Revenue> resultList = query.getResultList();
        List<RevenueVO> voList = RevenueMapper.INSTANCE.to(resultList);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    public int getTodayPay(){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
    
        sql.append("SELECT IFNULL(SUM(pay), 0) FROM revenue WHERE SUBSTR(create_date_time, 1, 10) = :date");
        params.put("date", LocalDate.now().toString());
    
        Query query = em.createNativeQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        Object result = query.getSingleResult();
        return ((Number) result).intValue();
    }
    
    public int getMonthPay(){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        
        sql.append(" SELECT IFNULL(SUM(pay), 0) FROM revenue ");
        sql.append(" WHERE SUBSTR(create_date_time, 1, 10) >= :firstDay ");
        sql.append(" and SUBSTR(create_date_time, 1, 10) <= :lastDay ");
        params.put("firstDay", firstDay);
        params.put("lastDay", lastDay);
        
        Query query = em.createNativeQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        Object result = query.getSingleResult();
        return ((Number) result).intValue();
    }
    
}
