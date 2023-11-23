package com.soho.pos.service;

import com.soho.pos.dao.*;
import com.soho.pos.dto.PurchaseDTO;
import com.soho.pos.dto.ReturnedDTO;
import com.soho.pos.entity.*;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import com.soho.pos.mapper.PurchaseMapper;
import com.soho.pos.mapper.ReturnedMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.PurchaseVO;
import com.soho.pos.vo.ReturnedVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReturnedService extends BaseService<Returned, Long, ReturnedDAO> {
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private ReturnedHistoryDAO historyDAO;
    @Autowired
    private InventoryDAO inventoryDAO;
    
    public PageResult findPageQuery(ReturnedDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Returned where 1 = 1 ");
        
        if (rq.getSizeId() != null) {
            sql.append(" and sizeId = :sizeId ");
            params.put("sizeId", rq.getSizeId());
        }
        
        if (rq.getCompanyId() != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }
        
        if (StringUtils.isNotBlank(rq.getProdNo())) {
            sql.append(" and prodNo = :prodNo ");
            params.put("prodNo", rq.getProdNo());
        }
        
        if (rq.getCnt() != null) {
            sql.append(" and cnt = :cnt ");
            params.put("cnt", rq.getCnt());
        }
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Returned> resultList = query.getResultList();
        List<ReturnedVO> voList = ReturnedMapper.INSTANCE.to(resultList);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void add(ReturnedDTO rq) throws PortalException {
        List<Returned> returnedList = new ArrayList<>();
        Inventory inventory = inventoryDAO.findByProductIdAndSizeId(rq.getProductId(), rq.getSizeId());
        if(inventory == null){
            throw new PortalException(ErrorText.EMPTY_INVENTORY_FAIL.getMsg());
        }
        Returned returned = dao.findByProductIdAndSizeId(rq.getProductId(), rq.getSizeId());
        if (returned != null) {
            returned.setCnt(returned.getCnt() + rq.getCnt());
            returnedList.add(returned);
        } else {
            returned = new Returned();
            returned.setSizeId(rq.getSizeId());
            returned.setCompanyId(rq.getCompanyId());
            returned.setProductId(rq.getProductId());
            returned.setCnt(rq.getCnt());
            returned.setProdNo(productDAO.findById(rq.getProductId()).get().getProdNo());
            returned.setPrice(rq.getPrice());
            returned.setTariff(rq.getTariff());
            returnedList.add(returned);
        }
        
        if (returned.getCnt() > inventory.getCnt()) {
            throw new PortalException(ErrorText.EXEC_INVENTORY_FAIL.getMsg());
        }
        dao.saveAllAndFlush(returnedList);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(ReturnedDTO rq) throws PortalException {
        Returned returned = dao.findById(rq.getId()).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        Inventory inventory = inventoryDAO.findByProductIdAndSizeId(returned.getProductId(), returned.getSizeId());
        if (rq.getCnt() > inventory.getCnt()) {
            throw new PortalException(ErrorText.EXEC_INVENTORY_FAIL.getMsg());
        }
        returned.setCnt(rq.getCnt());
        dao.update(returned);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public String saveBack(String returnedDate, String companyOrderNo) throws PortalException {
        LocalDate date = StringUtils.isNotBlank(returnedDate) ? LocalDate.parse(returnedDate, DateTimeFormatter.ISO_DATE) : LocalDate.now();
        String maxNo = historyDAO.findMaxNo();
        String str = maxNo.replaceAll("order", "");
        String buyNo = "order" + StringUtils.leftPad((Integer.parseInt(str) + 1) + "", 8, '0');
        List<Returned> returnedList = dao.findAll();
        List<ReturnedHistory> historyList = new ArrayList<>();
        for (Returned returned : returnedList) {
            ReturnedHistory history = new ReturnedHistory();
            history.setSizeId(returned.getSizeId());
            history.setCnt(returned.getCnt());
            history.setProdNo(returned.getProdNo());
            history.setProductId(returned.getProductId());
            history.setCompanyId(returned.getCompanyId());
            history.setBuyNo(buyNo);
            history.setPrice(returned.getPrice());
            history.setStatus("2");
            history.setTariff(returned.getTariff());
            history.setCompanyOrderNo(companyOrderNo);
            history.setReturnedDate(date);
            historyList.add(history);
        }
        historyDAO.saveAllAndFlush(historyList);
        return buyNo;
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void delBack() throws PortalException {
        dao.deleteAll();
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void del(Long id) throws PortalException {
        dao.deleteById(id);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void saveToInventory(String buyNo) throws PortalException {
        List<Returned> returnedList = dao.findAll();
        List<ReturnedHistory> historyList = historyDAO.findByBuyNo(buyNo);
        for (ReturnedHistory history : historyList) {
            history.setStatus("1");
        }
        List<Inventory> inventoryList = new ArrayList<>();
        for (Returned returned : returnedList) {
            Inventory inventory = inventoryDAO.findByProductIdAndSizeId(returned.getProductId(), returned.getSizeId());
            if (inventory == null) {
                continue;
            }
            int cnt = inventory.getCnt();
            //超出庫存 就直接用庫存數量
            if (returned.getCnt() > cnt) {
                returned.setCnt(cnt);
            }
            inventory.setCnt(cnt - returned.getCnt());
            inventoryList.add(inventory);
        }
        inventoryDAO.saveAllAndFlush(inventoryList);
        historyDAO.saveAllAndFlush(historyList);
        dao.deleteAll();
    }
}
