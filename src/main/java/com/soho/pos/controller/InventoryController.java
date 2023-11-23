
package com.soho.pos.controller;

import com.soho.pos.dto.InventoryDTO;
import com.soho.pos.dto.ProductDTO;
import com.soho.pos.entity.*;
import com.soho.pos.enums.PageView;
import com.soho.pos.mapper.PurchaseMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.InventoryService;
import com.soho.pos.service.ProductService;
import com.soho.pos.service.SizeChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/inventory")
    public String init(Model model) {
        List<SizeChart> sizeChartList = sizeChartService.getAll();
        List<Company> companyList = companyService.getAll("desc", "id");
        List<String> prodNoList = productService.getAll("desc", "id").stream()
                .filter(x -> "1".equals(x.getStatus()))
                .map(Product::getProdNo).distinct()
                .collect(Collectors.toList());
        model.addAttribute("sizeChartList", sizeChartList);
        model.addAttribute("companyList", companyList);
        model.addAttribute("prodNoList", prodNoList);
        return PageView.INVENTORY.getPath();
    }
    
    @PostMapping("/inventory/query")
    public ResponseEntity<?> query(@RequestBody InventoryDTO rq) {
        PageResult pageResult = inventoryService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/inventory/get/{id}")
    public String get(@PathVariable("id") Long id, Model model) {
        List<SizeChart> sizeChartList = sizeChartService.getAll();
        List<Company> companyList = companyService.getAll("desc", "id");
        List<Product> productList = productService.getAll("desc", "id").stream()
                .filter(x -> "1".equals(x.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("sizeChartList", sizeChartList);
        model.addAttribute("companyList", companyList);
        model.addAttribute("productList", productList);
        return PageView.INVENTORY.getPath() + "::inventory";
    }
    
    @PostMapping("/inventory/add")
    public ResponseEntity<?> add(@RequestBody InventoryDTO rq) {
        inventoryService.add(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/inventory/updateCnt")
    public ResponseEntity<?> updateCnt(@RequestBody List<Inventory> inventoryList) {
        inventoryService.updCnt(inventoryList);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @DeleteMapping("/inventory/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        inventoryService.del(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
