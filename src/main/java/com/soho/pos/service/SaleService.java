package com.soho.pos.service;

import com.soho.pos.dao.*;
import com.soho.pos.dto.SaleDTO;
import com.soho.pos.entity.*;
import com.soho.pos.mapper.SaleMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.SaleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService extends BaseService<Sale, Long, SaleDAO> {
    @Autowired
    private RevenueDAO revenueDAO;
    @Autowired
    private SaleDAO saleDAO;
    @Autowired
    private InventoryDAO inventoryDAO;
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private ProductRankDAO productRankDAO;
    
    public PageResult findPageQuery(SaleDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Sale where revenueId = :revenueId ");
        params.put("revenueId", rq.getRevenueId());
        
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Sale> resultList = query.getResultList();
        List<SaleVO> voList = SaleMapper.INSTANCE.to(resultList);
        long sum = voList.stream().mapToInt(x -> (x.getCnt() * x.getProduct().getPrice())).sum();
        Map<String, Long> map = new HashMap();
        map.put("total", sum);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw(), map);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void pay(List<SaleDTO> dtoList, HttpSession session) {
        List<Sale> saleList = new ArrayList<>();
        int total = dtoList.get(0).getTotal();
        int discount = dtoList.get(0).getDiscount();
        int bonus = dtoList.get(0).getBonus();
        int pay = dtoList.get(0).getPay();
        int promotion = dtoList.get(0).getPromotion();
        String payMethod = dtoList.get(0).getPayMethod();
        //消費金額紀錄
        Revenue revenue = new Revenue();
        revenue.setTotal(total);
        revenue.setDiscount(discount);
        revenue.setBonus(bonus);
        revenue.setPay(pay);
        revenue.setPromotion(promotion);
        revenue.setPayMethod(payMethod);
        
        Activity activity = activityDAO.findById(1L).get();
        Member member = (Member) session.getAttribute("member");
        //有輸入會員的話
        if (member != null) {
            int points = member.getPoints() - bonus;
            //有開紅利活動
            if ("1".equals(activity.getBonusType())) {
                int payPoints = (int) Math.floor(pay / activity.getBonusMoney());
                member.setPoints(points + payPoints);
                revenue.setPoints(payPoints);
            } else {
                member.setPoints(points);
                revenue.setPoints(0);
            }
            memberDAO.update(member);
            revenue.setMemberId(member.getId());
        }
        revenueDAO.insert(revenue);
        
        for (SaleDTO dto : dtoList) {
            //更新庫存
            Inventory inventory = inventoryDAO.findById(dto.getInventoryId()).get();
            inventory.setCnt(inventory.getCnt() - dto.getCnt());
            inventoryDAO.update(inventory);
            
            ProductRank productRank = productRankDAO.findByProductId(inventory.getProductId(), LocalDate.now());
            if (productRank != null) {
                productRank.setCnt(productRank.getCnt() + dto.getCnt());
                productRankDAO.update(productRank);
            } else {
                productRank = new ProductRank();
                productRank.setSaleDate(LocalDate.now());
                productRank.setProductId(inventory.getProductId());
                productRank.setCnt(dto.getCnt());
                productRank.setCompanyId(inventory.getCompanyId());
                productRankDAO.insert(productRank);
            }
            //建立消費明細
            Sale sale = new Sale();
            sale.setSizeId(inventory.getSizeId());
            sale.setCnt(dto.getCnt());
            sale.setProductId(inventory.getProductId());
            sale.setCompanyId(inventory.getCompanyId());
            sale.setRevenueId(revenue.getId());
            saleList.add(sale);
        }
        saleDAO.saveAllAndFlush(saleList);
    }
}
