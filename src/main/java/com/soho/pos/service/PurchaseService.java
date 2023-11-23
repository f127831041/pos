package com.soho.pos.service;

import com.soho.pos.dao.InventoryDAO;
import com.soho.pos.dao.ProductDAO;
import com.soho.pos.dao.PurchaseDAO;
import com.soho.pos.dao.PurchaseHistoryDAO;
import com.soho.pos.dto.PurchaseDTO;
import com.soho.pos.entity.Inventory;
import com.soho.pos.entity.Purchase;
import com.soho.pos.entity.PurchaseHistory;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import com.soho.pos.mapper.PurchaseMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.PurchaseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseService extends BaseService<Purchase, Long, PurchaseDAO> {
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private PurchaseHistoryDAO historyDAO;
    @Autowired
    private InventoryDAO inventoryDAO;
    
    public PageResult findPageQuery(PurchaseDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Purchase where 1 = 1 ");
        
        if (rq.getSizeId() != null) {
            sql.append(" and sizeId = :sizeId ");
            params.put("sizeId", rq.getSizeId());
        }
        
        if (rq.getCompanyId() != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }
        
        if (StringUtils.isNotBlank(rq.getProdNo())) {
            sql.append(" and prodNo like :prodNo ");
            params.put("prodNo", "%" + rq.getProdNo() + "%");
        }
        
        if (rq.getCnt() != null) {
            sql.append(" and cnt = :cnt ");
            params.put("cnt", rq.getCnt());
        }
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Purchase> resultList = query.getResultList();
        List<PurchaseVO> voList = PurchaseMapper.INSTANCE.to(resultList);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void add(PurchaseDTO rq) throws PortalException {
        List<Purchase> purchaseList = new ArrayList<>();
        
        Purchase purchase = dao.findByProductIdAndSizeId(rq.getProductId(), rq.getSizeId());
        if (purchase != null) {
            purchase.setCnt(purchase.getCnt() + rq.getCnt());
            purchaseList.add(purchase);
        } else {
            purchase = new Purchase();
            purchase.setSizeId(rq.getSizeId());
            purchase.setCompanyId(rq.getCompanyId());
            purchase.setProductId(rq.getProductId());
            purchase.setCnt(rq.getCnt());
            purchase.setProdNo(productDAO.findById(rq.getProductId()).get().getProdNo());
            purchase.setPrice(rq.getPrice());
            purchase.setTariff(rq.getTariff());
            purchaseList.add(purchase);
        }
        dao.saveAllAndFlush(purchaseList);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(PurchaseDTO rq) throws PortalException {
        Purchase purchase = dao.findById(rq.getId()).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        purchase.setCnt(rq.getCnt());
        dao.update(purchase);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public String buy(String purchaseDate, String companyOrderNo) throws PortalException {
        LocalDate date = StringUtils.isNotBlank(purchaseDate) ? LocalDate.parse(purchaseDate, DateTimeFormatter.ISO_DATE) : LocalDate.now();
        String maxNo = historyDAO.findMaxNo();
        String str = maxNo.replaceAll("order", "");
        String buyNo = "order" + StringUtils.leftPad((Integer.parseInt(str) + 1) + "", 8, '0');
        List<Purchase> purchaseList = dao.findAll();
        List<PurchaseHistory> historyList = new ArrayList<>();
        for (Purchase purchase : purchaseList) {
            PurchaseHistory history = new PurchaseHistory();
            history.setSizeId(purchase.getSizeId());
            history.setCnt(purchase.getCnt());
            history.setProdNo(purchase.getProdNo());
            history.setProductId(purchase.getProductId());
            history.setCompanyId(purchase.getCompanyId());
            history.setBuyNo(buyNo);
            history.setPrice(purchase.getPrice());
            history.setStatus("2");
            history.setTariff(purchase.getTariff());
            history.setCompanyOrderNo(companyOrderNo);
            history.setPurchaseDate(date);
            historyList.add(history);
        }
        historyDAO.saveAllAndFlush(historyList);
        return buyNo;
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void delBuy() throws PortalException {
        dao.deleteAll();
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void del(Long id) throws PortalException {
        dao.deleteById(id);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void saveToInventory(String buyNo) throws PortalException {
        List<Purchase> purchaseList = dao.findAll();
        List<PurchaseHistory> historyList = historyDAO.findByBuyNo(buyNo);
        for (PurchaseHistory history : historyList) {
            history.setStatus("1");
        }
        List<Inventory> inventoryList = new ArrayList<>();
        for (Purchase purchase : purchaseList) {
            Inventory inventory = inventoryDAO.findByProductIdAndSizeId(purchase.getProductId(), purchase.getSizeId());
            if (inventory == null) {
                inventory = new Inventory();
                inventory.setSizeId(purchase.getSizeId());
                inventory.setCnt(purchase.getCnt());
                inventory.setProdNo(purchase.getProdNo());
                inventory.setCompanyId(purchase.getCompanyId());
                inventory.setProductId(purchase.getProductId());
            } else {
                inventory.setCnt(inventory.getCnt() + purchase.getCnt());
            }
            inventoryList.add(inventory);
        }
        inventoryDAO.saveAllAndFlush(inventoryList);
        historyDAO.saveAllAndFlush(historyList);
        dao.deleteAll();
    }
}
