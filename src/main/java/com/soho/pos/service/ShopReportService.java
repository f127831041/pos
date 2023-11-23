package com.soho.pos.service;

import com.soho.pos.dto.ShopReportDTO;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.RefundVO;
import com.soho.pos.vo.RevenueVO;
import com.soho.pos.vo.ShopReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopReportService {
    @PersistenceContext
    protected EntityManager em;
    
    public PageResult findPageQuery(ShopReportDTO rq) {
        List<RevenueVO> revenueList = getRevenue(rq.getDate());
        List<RefundVO> refundList = getRefund(rq.getDate());
        List<String> dateList = new ArrayList<>();
        
        for (RevenueVO revenue : revenueList) {
            dateList.add(revenue.getCreateDate());
        }
        
        // 將 refundList 中的日期加入 mergedDateList
        for (RefundVO refund : refundList) {
            dateList.add(refund.getCreateDate());
        }
        
        dateList = dateList.stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        if ("asc".equals(rq.getOrderDir())) {
            dateList = dateList.stream().distinct().sorted().collect(Collectors.toList());
        }
        
        Map<String, RevenueVO> revenueMap = new HashMap<>();
        for (RevenueVO vo : revenueList) {
            RevenueVO data = revenueMap.getOrDefault(vo.getCreateDate(), new RevenueVO());
            data.setTotal(vo.getTotal() + data.getTotal());
            data.setDiscount(vo.getDiscount() + data.getDiscount());
            data.setBonus(vo.getBonus() + data.getBonus());
            data.setPay(vo.getPay() + data.getPay());
            data.setPoints(vo.getPoints() + data.getPoints());
            data.setPromotion(vo.getPromotion() + data.getPromotion());
            data.setCash("1".equals(vo.getPayMethod()) ? vo.getPay() + data.getCash() : data.getCash());
            data.setLine("2".equals(vo.getPayMethod()) ? vo.getPay() + data.getLine() : data.getLine());
            data.setCard("3".equals(vo.getPayMethod()) ? vo.getPay() + data.getCard() : data.getCard());
            revenueMap.put(vo.getCreateDate(), data);
        }
        
        Map<String, RefundVO> refundMap = refundList.stream().collect(Collectors.toMap(RefundVO::getCreateDate, Function.identity()));
        
        List<ShopReportVO> voList = new ArrayList<>();
        Map<String, Integer> countMap = new HashMap<>();
        for (String date : dateList) {
            RevenueVO revenue = revenueMap.getOrDefault(date, new RevenueVO());
            RefundVO refund = refundMap.getOrDefault(date, new RefundVO());
            ShopReportVO vo = new ShopReportVO();
            vo.setCreateDate(date);
            vo.setDiscount(revenue.getDiscount());
            vo.setBonus(revenue.getBonus());
            vo.setPromotion(revenue.getPromotion());
            vo.setRevenueTotal(revenue.getTotal());
            vo.setRevenuePay(revenue.getPay());
            vo.setRefundTotal(refund.getTotal());
            vo.setRefundPay(refund.getPay());
            vo.setCash(revenue.getCash());
            vo.setLine(revenue.getLine());
            vo.setCard(revenue.getCard());
            vo.setPay(revenue.getPay() - refund.getPay());
            voList.add(vo);
            
            int discount = countMap.getOrDefault("discount", 0);
            discount += revenue.getDiscount();
            countMap.put("discount", discount);
            
            int bonus = countMap.getOrDefault("bonus", 0);
            bonus += revenue.getBonus();
            countMap.put("bonus", bonus);
            
            int promotion = countMap.getOrDefault("promotion", 0);
            promotion += revenue.getPromotion();
            countMap.put("promotion", promotion);
            
            int revenuePay = countMap.getOrDefault("revenuePay", 0);
            revenuePay += revenue.getPay();
            countMap.put("revenuePay", revenuePay);
            
            int refundPay = countMap.getOrDefault("refundPay", 0);
            refundPay += refund.getPay();
            countMap.put("refundPay", refundPay);
            
            int cash = countMap.getOrDefault("cash", 0);
            cash += revenue.getCash();
            countMap.put("cash", cash);
            
            int line = countMap.getOrDefault("line", 0);
            line += revenue.getLine();
            countMap.put("line", line);
            
            int card = countMap.getOrDefault("card", 0);
            card += revenue.getCard();
            countMap.put("card", card);
            
            int pay = countMap.getOrDefault("pay", 0);
            pay += revenue.getPay() - refund.getPay();
            countMap.put("pay", pay);
        }
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw(), countMap);
    }
    
    public List<RevenueVO> getRevenue(String date) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" select substr(create_date_time,1,10), ifNull(sum(bonus),0), ifNull(sum(discount),0), ifNull(sum(pay),0), ifNull(sum(total),0), ifNull(sum(promotion),0), pay_method ");
        sql.append(" from revenue where 1 = 1 ");
        
        if (StringUtils.isNotBlank(date)) {
            String sDate = date.split("至")[0].trim();
            String eDate = date.split("至")[1].trim();
            sql.append(" and substr(create_date_time, 1, 10) >= :sDate ");
            params.put("sDate", sDate);
            
            sql.append(" and substr(create_date_time, 1, 10) <= :eDate ");
            params.put("eDate", eDate);
        }
        
        sql.append(" group by substr(create_date_time, 1, 10), pay_method ");
        sql.append(" order by create_date_time desc");
        
        Query query = em.createNativeQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Object[]> resultList = query.getResultList();
        List<RevenueVO> voList = new ArrayList<>();
        for (Object[] result : resultList) {
            String createDate = (String) result[0];
            int bonus = Integer.parseInt(result[1].toString());
            int discount = Integer.parseInt(result[2].toString());
            int pay = Integer.parseInt(result[3].toString());
            int total = Integer.parseInt(result[4].toString());
            int promotion = Integer.parseInt(result[5].toString());
            String payMethod = result[6].toString();
            RevenueVO vo = new RevenueVO();
            vo.setTotal(total);
            vo.setDiscount(discount);
            vo.setBonus(bonus);
            vo.setPay(pay);
            vo.setPromotion(promotion);
            vo.setCreateDate(createDate);
            vo.setPayMethod(payMethod);
            voList.add(vo);
        }
        return voList;
    }
    
    public List<RefundVO> getRefund(String date) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select substr(create_date_time,1,10), ifNull(sum(total),0), ifNull(sum(pay),0) ");
        sql.append(" from refund where 1 = 1 ");
        
        if (StringUtils.isNotBlank(date)) {
            String sDate = date.split("至")[0].trim();
            String eDate = date.split("至")[1].trim();
            sql.append(" and substr(create_date_time, 1, 10) >= :sDate ");
            params.put("sDate", sDate);
            
            sql.append(" and substr(create_date_time, 1, 10) <= :eDate ");
            params.put("eDate", eDate);
        }
        
        sql.append(" group by substr(create_date_time, 1, 10) ");
        sql.append(" order by create_date_time desc");
        
        Query query = em.createNativeQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Object[]> resultList = query.getResultList();
        List<RefundVO> voList = new ArrayList<>();
        for (Object[] result : resultList) {
            String createDate = (String) result[0];
            int total = Integer.parseInt(result[1].toString());
            int pay = Integer.parseInt(result[2].toString());
            RefundVO vo = new RefundVO();
            vo.setTotal(total);
            vo.setPay(pay);
            vo.setCreateDate(createDate);
            voList.add(vo);
        }
        return voList;
    }
}
