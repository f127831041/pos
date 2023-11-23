package com.soho.pos.service;

import com.soho.pos.dao.*;
import com.soho.pos.dto.RefundDTO;
import com.soho.pos.dto.RefundRecordDTO;
import com.soho.pos.dto.SaleDTO;
import com.soho.pos.entity.*;
import com.soho.pos.mapper.RefundMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.RefundVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundService extends BaseService<Refund, Long, RefundDAO> {
    @Autowired
    private RefundRecordDAO recordDAO;
    @Autowired
    private InventoryDAO inventoryDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private MemberDAO memberDAO;
    
    public PageResult findPageQuery(RefundDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append("from Refund where 1 = 1 ");
        
        if (StringUtils.isNotBlank(rq.getDate())) {
            String sDate = rq.getDate().split("至")[0].trim();
            String eDate = rq.getDate().split("至")[1].trim();
            sql.append(" and substr(createDateTime, 1, 10) >= :sDate ");
            params.put("sDate", sDate);
            
            sql.append(" and substr(createDateTime, 1, 10) <= :eDate ");
            params.put("eDate", eDate);
        }
        
        if (StringUtils.isNotBlank(rq.getPhone())) {
            Member member = memberDAO.findByPhone(rq.getPhone());
            long id = member == null ? -1 : member.getId();
            sql.append(" and memberId = :memberId ");
            params.put("memberId", id);
        }
        
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Refund> resultList = query.getResultList();
        List<RefundVO> voList = RefundMapper.INSTANCE.to(resultList);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void save(List<RefundDTO> dtoList, HttpSession session) {
        int total = dtoList.get(0).getTotal();
        int pay = dtoList.get(0).getPay();
        int bonus = dtoList.get(0).getBonus();
        String payMethod = dtoList.get(0).getPayMethod();
        
        Refund refund = new Refund();
        refund.setPay(pay);
        refund.setTotal(total);
        refund.setPayMethod(payMethod);
        refund.setBonus(bonus);
        
        Member member = (Member) session.getAttribute("member");
        //有輸入會員的話
        if (member != null) {
            refund.setMemberId(member.getId());
        }
        dao.insert(refund);
        
        List<RefundRecord> refundRecordList = new ArrayList<>();
        List<Inventory> inventoryList = new ArrayList<>();
        for (RefundDTO dto : dtoList) {
            //更新庫存
            Inventory inventory = inventoryDAO.findByProductIdAndSizeId(dto.getProductId(), dto.getSizeId());
            Product product = productDAO.findById(dto.getProductId()).get();
            if (inventory != null) {
                inventory.setCnt(inventory.getCnt() + dto.getCnt());
            } else {
                inventory = new Inventory();
                inventory.setCnt(dto.getCnt());
                inventory.setProductId(product.getId());
                inventory.setCompanyId(product.getCompanyId());
                inventory.setSizeId(dto.getSizeId());
                inventory.setProdNo(product.getProdNo());
            }
            inventoryList.add(inventory);
            
            //建立退費明細
            RefundRecord record = new RefundRecord();
            record.setSizeId(dto.getSizeId());
            record.setCnt(dto.getCnt());
            record.setProductId(dto.getProductId());
            record.setCompanyId(product.getCompanyId());
            record.setRefundId(refund.getId());
            refundRecordList.add(record);
        }
        recordDAO.saveAllAndFlush(refundRecordList);
        inventoryDAO.saveAllAndFlush(inventoryList);
    }
    
    public int getTodayRefund(){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT IFNULL(SUM(pay), 0) FROM refund WHERE SUBSTR(create_date_time, 1, 10) = :date");
        params.put("date", LocalDate.now().toString());
        
        Query query = em.createNativeQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        Object result = query.getSingleResult();
        return ((Number) result).intValue();
    }
    
    public int getMonthRefund(){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        
        sql.append(" SELECT IFNULL(SUM(pay), 0) FROM refund ");
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
