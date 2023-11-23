

package com.soho.pos.controller;

import com.soho.pos.dto.SaleDTO;
import com.soho.pos.entity.*;
import com.soho.pos.enums.PageView;
import com.soho.pos.mapper.InventoryMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.*;
import com.soho.pos.vo.InventoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class SaleController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private SaleService saleService;
    
    @GetMapping("/sale")
    public String init(Model model) {
        List<Company> companyList = companyService.getAll("desc", "id");
        List<Product> productList = productService.getAll("desc", "id").stream()
                .filter(x -> "1".equals(x.getStatus()))
                .collect(Collectors.toList());
//        List<Long> inventoryList = inventoryService.getAll().stream().filter(x -> x.getCnt() > 0).map(Inventory::getProductId).distinct().collect(Collectors.toList());
//        productList = productList.stream().filter(x -> inventoryList.contains(x.getId())).collect(Collectors.toList());
        
        model.addAttribute("productList", productList);
        model.addAttribute("companyList", companyList);
        model.addAttribute("activity", activityService.get(1L));
        return PageView.SALE.getPath();
    }
    
    @PostMapping("/sale/query")
    public ResponseEntity<?> query(@RequestBody SaleDTO rq) {
        PageResult pageResult = saleService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/sale/get/product/{productId}")
    public ResponseEntity<?> getInventoryProduct(@PathVariable("productId") Long productId) {
        List<Inventory> inventoryList = inventoryService.findByProductId(productId);
        List<InventoryVO> voList = InventoryMapper.INSTANCE.to(inventoryList.stream().filter(x -> x.getCnt() > 0).sorted(Comparator.comparing(Inventory::getSizeId)).collect(Collectors.toList()));
        return new ResponseEntity<>(Result.success(voList), HttpStatus.OK);
    }
    
    
    @GetMapping("/sale/get/sizeChart")
    public ResponseEntity<?> getSizeChart() {
        List<SizeChart> sizeChartList = sizeChartService.getAll("asc", "id");
        return new ResponseEntity<>(Result.success(sizeChartList), HttpStatus.OK);
    }
    
    @PostMapping("/sale/billPay")
    public ResponseEntity<?> billPay(@RequestBody List<SaleDTO> dtoList, HttpSession session) {
        saleService.pay(dtoList, session);
        session.removeAttribute("member");
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
