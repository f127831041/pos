package com.soho.pos.service;

import com.soho.pos.dao.InventoryDAO;
import com.soho.pos.dao.ProductDAO;
import com.soho.pos.dto.InventoryDTO;
import com.soho.pos.dto.ProductDTO;
import com.soho.pos.entity.Inventory;
import com.soho.pos.entity.Product;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import com.soho.pos.mapper.InventoryMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.InventoryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryService extends BaseService<Inventory, Long, InventoryDAO> {
    @Autowired
    private ProductDAO productDAO;
    
    public PageResult findPageQuery(InventoryDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Inventory where 1 = 1 ");
        
        if (rq.getSizeId() != null) {
            sql.append(" and sizeId = :sizeId ");
            params.put("sizeId", rq.getSizeId());
        }
        
        if (rq.getCompanyId() != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }
        
        if (rq.getCnt() != null) {
            sql.append(" and cnt = :cnt ");
            params.put("cnt", rq.getCnt());
        }
        
        if (StringUtils.isNotBlank(rq.getProdNo())) {
            sql.append(" and prodNo = :prodNo ");
            params.put("prodNo", rq.getProdNo());
        }
        
        List<String> productColumns = Arrays.asList("name", "color", "design");
        if (!productColumns.contains(rq.getOrderColumn())) {
            sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        }
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Inventory> resultList = query.getResultList();
        List<InventoryVO> voList = InventoryMapper.INSTANCE.to(resultList);
        if ("name".equals(rq.getOrderColumn())) {
            Comparator<InventoryVO> comparator = Comparator.comparing(x -> x.getProduct().getName());
            if("desc".equals(rq.getOrderDir())){
                comparator = comparator.reversed();
            }
            voList = voList.stream().sorted(comparator).collect(Collectors.toList());
        } else if ("design".equals(rq.getOrderColumn())) {
            Comparator<InventoryVO> comparator = Comparator.comparing(x -> x.getProduct().getDesign(), Comparator.nullsLast(String::compareTo));
            if("desc".equals(rq.getOrderDir())){
                comparator = comparator.reversed();
            }
            voList = voList.stream().sorted(comparator).collect(Collectors.toList());
        } else if ("color".equals(rq.getOrderColumn())) {
            Comparator<InventoryVO> comparator = Comparator.comparing(x -> x.getProduct().getColor(), Comparator.nullsLast(String::compareTo));
            if("desc".equals(rq.getOrderDir())){
                comparator = comparator.reversed();
            }
            voList = voList.stream().sorted(comparator).collect(Collectors.toList());
        }
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void add(InventoryDTO rq) throws PortalException {
        Inventory inventory = dao.findByProductIdAndSizeId(rq.getProductId(), rq.getSizeId());
        if (inventory != null) {
            throw new PortalException(ErrorText.PRODUCT_FAIL.getMsg());
        }
        Product product = productDAO.findById(rq.getProductId()).get();
        inventory = new Inventory();
        inventory.setProdNo(product.getProdNo());
        inventory.setProductId(rq.getProductId());
        inventory.setCompanyId(rq.getCompanyId());
        inventory.setSizeId(rq.getSizeId());
        inventory.setCnt(rq.getCnt());
        dao.insert(inventory);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void updCnt(List<Inventory> inventoryList) throws PortalException {
        List<Inventory> insertList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            Inventory dbInventory = dao.getReferenceById(inventory.getId());
            dbInventory.setCnt(inventory.getCnt());
            insertList.add(dbInventory);
        }
        dao.saveAllAndFlush(insertList);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void del(Long id) throws PortalException {
        dao.deleteById(id);
    }
    
    public List<Inventory> findByProductId(long productId) {
        return dao.findByProductId(productId);
    }
    
}
