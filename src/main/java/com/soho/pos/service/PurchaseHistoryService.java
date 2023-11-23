package com.soho.pos.service;

import com.soho.pos.dao.CompanyDAO;
import com.soho.pos.dao.InventoryDAO;
import com.soho.pos.dao.PurchaseHistoryDAO;
import com.soho.pos.dto.PurchaseHistoryDTO;
import com.soho.pos.entity.Inventory;
import com.soho.pos.entity.PurchaseHistory;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import com.soho.pos.mapper.PurchaseHistoryMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.PurchaseHistoryVO;
import com.soho.pos.vo.PurchaseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseHistoryService extends BaseService<PurchaseHistory, Long, PurchaseHistoryDAO> {
    
    @Autowired
    private InventoryDAO inventoryDAO;
    @Autowired
    private CompanyDAO companyDAO;
    
    public PageResult findPageQuery(PurchaseHistoryDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append("select id, company_id, buy_no, count(id), status, company_order_no, purchase_date from purchase_history where 1 = 1 ");
        
        if (StringUtils.isNotBlank(rq.getDate())) {
            String sDate = rq.getDate().split("至")[0].trim();
            String eDate = rq.getDate().split("至")[1].trim();
            sql.append(" and purchase_date >= :sDate ");
            params.put("sDate", LocalDate.parse(sDate));
            
            sql.append(" and purchase_date <= :eDate ");
            params.put("eDate", LocalDate.parse(eDate));
        }
        
        if (rq.getCompanyId() != null) {
            sql.append(" and company_id = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }
    
        if (StringUtils.isNotBlank(rq.getCompanyOrderNo())) {
            sql.append(" and company_order_no >= :companyOrderNo ");
            params.put("companyOrderNo", rq.getCompanyOrderNo());
        }
        
        sql.append(" group by buy_no ");
        sql.append(" order by purchase_date desc ");
        
        Query query = em.createNativeQuery(sql.toString());
        
        SqlUtils.getQueryWithParameters(query, params);
        List<Object[]> resultList = query.getResultList();
        List<PurchaseHistoryVO> voList = new ArrayList<>();
        for (Object[] result : resultList) {
            long id = (Long.parseLong(result[0].toString()));
            long companyId = (Long.parseLong(result[1].toString()));
            String buyNo = (String) result[2];
            int num = (Integer.parseInt(result[3].toString()));
            String status = (String) result[4];
            String companyOrderNo = (String) result[5];
            String purchaseDate = result[6].toString();
            PurchaseHistoryVO vo = new PurchaseHistoryVO();
            vo.setId(id);
            vo.setNum(num);
            vo.setStatus(status);
            vo.setCompany(companyDAO.findById(companyId).get());
            vo.setBuyNo(buyNo);
            vo.setCompanyOrderNo(companyOrderNo);
            vo.setPurchaseDate(purchaseDate);
            voList.add(vo);
        }
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void saveToInventory(String buyNo) throws PortalException {
        List<Inventory> inventoryList = new ArrayList<>();
        List<PurchaseHistory> historyList = dao.findByBuyNo(buyNo);
        for (PurchaseHistory history : historyList) {
            history.setStatus("1");
            Inventory inventory = inventoryDAO.findByProductIdAndSizeId(history.getProductId(), history.getSizeId());
            if (inventory == null) {
                inventory = new Inventory();
                inventory.setSizeId(history.getSizeId());
                inventory.setCnt(history.getCnt());
                inventory.setProdNo(history.getProdNo());
                inventory.setProductId(history.getProductId());
                inventory.setCompanyId(history.getCompanyId());
            } else {
                inventory.setCnt(inventory.getCnt() + history.getCnt());
            }
            inventoryList.add(inventory);
        }
        inventoryDAO.saveAllAndFlush(inventoryList);
        dao.saveAllAndFlush(historyList);
    }
    
    
    public PageResult findByDataInfo(PurchaseHistoryDTO rq) {
        List<PurchaseHistory> historyList = dao.findByBuyNo(rq.getBuyNo());
        List<PurchaseVO> voList = PurchaseHistoryMapper.INSTANCE.to(historyList);
        long total = voList.stream().mapToInt(x -> (x.getCnt() * x.getPrice())).sum();
        long tariffPay = 0;
        for (PurchaseVO vo : voList) {
            int cnt = vo.getCnt();
            int price = vo.getPrice();
            int sum = cnt * price;
            int tariff = vo.getTariff();
            int pay = (int) Math.floor(sum * (0.01 * (tariff)));
            tariffPay += (sum + pay);
        }
        Map<String, Long> map = new HashMap();
        map.put("total", total);
        map.put("tariffPay", tariffPay);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw(), map);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(PurchaseHistoryDTO rq) throws PortalException {
        PurchaseHistory history = dao.findById(rq.getId()).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        history.setPrice(rq.getPrice());
        dao.update(history);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void del(Long id) throws PortalException {
        PurchaseHistory history = dao.findById(id).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        Inventory inventory = inventoryDAO.findByProductIdAndSizeId(history.getProductId(), history.getSizeId());
        if (inventory != null && "1".equals(history.getStatus())) {
            int cnt = inventory.getCnt() - history.getCnt();
            inventory.setCnt(Math.max(cnt, 0));
            inventoryDAO.update(inventory);
        }
        dao.deleteById(id);
    }
}
