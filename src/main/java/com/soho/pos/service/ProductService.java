package com.soho.pos.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.soho.pos.dao.InventoryDAO;
import com.soho.pos.dao.ProductDAO;
import com.soho.pos.dao.SizeChartDAO;
import com.soho.pos.dto.ProductDTO;
import com.soho.pos.entity.Inventory;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.SizeChart;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import com.soho.pos.mapper.ProductMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService extends BaseService<Product, Long, ProductDAO> {
    @Autowired
    private InventoryDAO inventoryDAO;
    @Autowired
    private SizeChartDAO sizeChartDAO;
    
    public PageResult findPageQuery(ProductDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Product where 1 = 1 ");
        
        if (rq.getCompanyId() != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }
        
        if (StringUtils.isNotBlank(rq.getProdNo())) {
            sql.append(" and prodNo like :prodNo ");
            params.put("prodNo", "%" + rq.getProdNo() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getName())) {
            sql.append(" and name like :name ");
            params.put("name", "%" + rq.getName() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getColor())) {
            sql.append(" and color like :color ");
            params.put("color", "%" + rq.getColor() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getDesign())) {
            sql.append(" and design like :design ");
            params.put("design", "%" + rq.getDesign() + "%");
        }
        
        if (rq.getPurchasePrice() != null) {
            sql.append(" and purchasePrice = :purchasePrice ");
            params.put("purchasePrice", rq.getPurchasePrice());
        }
        
        if (rq.getPrice() != null) {
            sql.append(" and price = :price ");
            params.put("price", rq.getPrice());
        }
        
        if (StringUtils.isNotBlank(rq.getStatus())) {
            sql.append(" and status = :status ");
            params.put("status", rq.getStatus());
        }
        
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Product> resultList = query.getResultList();
        List<ProductVO> voList = ProductMapper.INSTANCE.to(resultList);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    public Product findByProduct(String prodNo, String name, String design, String color) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Product where prodNo = :prodNo and name = :name");
        
        params.put("prodNo", prodNo);
        params.put("name", name);
        
        if (StringUtils.isNotBlank(design)) {
            sql.append(" and design = :design ");
            params.put("design", design);
        }
        
        if (StringUtils.isNotBlank(color)) {
            sql.append(" and color = :color ");
            params.put("color", color);
        }
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Product> resultList = query.getResultList();
        return resultList.size() > 0 ? resultList.get(0) : null;
    }
    
    public List<Product> getData(ProductDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Product where status = '1' ");
        
        if (rq.getCompanyId() != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }
        
        if (StringUtils.isNotBlank(rq.getProdNo())) {
            sql.append(" and prodNo = :prodNo ");
            params.put("prodNo", rq.getProdNo());
        }
        
        if (StringUtils.isNotBlank(rq.getName())) {
            sql.append(" and name = :name ");
            params.put("name", rq.getName());
        }
        
        if (StringUtils.isNotBlank(rq.getDesign())) {
            sql.append(" and design = :design ");
            params.put("design", rq.getDesign());
        }
        
        if (StringUtils.isNotBlank(rq.getColor())) {
            sql.append(" and color = :color ");
            params.put("color", rq.getColor());
        }
    
        sql.append(" order by id desc");
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Product> resultList = query.getResultList();
        return resultList;
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void add(ProductDTO rq) throws PortalException {
        Product product = findByProduct(rq.getProdNo(), rq.getName(), rq.getDesign(), rq.getColor());
        if (product != null) {
            throw new PortalException(ErrorText.PRODUCT_FAIL.getMsg());
        }
        product = new Product();
        product.setCompanyId(rq.getCompanyId());
        product.setProdNo(rq.getProdNo());
        product.setName(rq.getName());
        product.setDesign(rq.getDesign());
        product.setColor(rq.getColor());
        product.setPurchasePrice(rq.getPurchasePrice());
        product.setPrice(rq.getPrice());
        product.setStatus(rq.getStatus());
        dao.insert(product);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(ProductDTO rq) throws PortalException {
        Product product = findByProduct(rq.getProdNo(), rq.getName(), rq.getDesign(), rq.getColor());
        if (product != null && product.getId().longValue() != rq.getId()) {
            throw new PortalException(ErrorText.PRODUCT_FAIL.getMsg());
        }
        product = dao.findById(rq.getId()).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        product.setCompanyId(rq.getCompanyId());
        product.setProdNo(rq.getProdNo());
        product.setName(rq.getName());
        product.setDesign(rq.getDesign());
        product.setColor(rq.getColor());
        product.setPurchasePrice(rq.getPurchasePrice());
        product.setPrice(rq.getPrice());
        product.setStatus(rq.getStatus());
        dao.update(product);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void del(Long id) throws PortalException {
        List<Inventory> inventoryList = inventoryDAO.findByProductId(id);
        Map<Long, SizeChart> sizeMap = sizeChartDAO.findAll().stream().collect(Collectors.toMap(SizeChart::getId, Function.identity()));
        //產品庫存
        for (Inventory inventory : inventoryList) {
            if (inventory.getCnt() > 0) {
                throw new PortalException(ErrorText.INVENTORY_FAIL.getMsg("尺寸" + sizeMap.get(inventory.getSizeId())));
            }
        }
        inventoryDAO.deleteAllInBatch(inventoryList);
        dao.deleteById(id);
    }
    
    public void barcode(long id) throws Exception {
        Product product = dao.findById(id).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        String fileName = UUID.randomUUID() + ".png";
        File outputFile = new File("C:\\logs\\" + fileName);
        
        Writer writer = new Code128Writer();
        BitMatrix bitMatrix = writer.encode("http://localhost:8080/pos/login", BarcodeFormat.CODE_128, 200, 80);
        
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
        outputStream.close();
    }
    
    public List<Product> findByCompanyId(long companyId) {
        return dao.findByCompanyId(companyId);
    }
    
}
