
package com.soho.pos.controller;

import com.soho.pos.dto.PurchaseDTO;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.Purchase;
import com.soho.pos.entity.SizeChart;
import com.soho.pos.enums.PageView;
import com.soho.pos.mapper.PurchaseMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.ProductService;
import com.soho.pos.service.PurchaseService;
import com.soho.pos.service.SizeChartService;
import org.apache.commons.lang3.StringUtils;
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
public class PurchaseController {
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private PurchaseService purchaseService;
    
    @GetMapping("/purchase")
    public String init(Model model) {
        List<SizeChart> sizeChartList = sizeChartService.getAll();
        List<Product> productList = productService.getAll("desc", "id");
        List<Company> companyList = companyService.getAll("desc", "id");
        
        List<String> prodNoList = productList.stream()
                .filter(x -> "1".equals(x.getStatus()))
                .map(Product::getProdNo).distinct()
                .collect(Collectors.toList());
        
        model.addAttribute("sizeChartList", sizeChartList);
        model.addAttribute("companyList", companyList);
        model.addAttribute("prodNoList", prodNoList);
        model.addAttribute("productList", productList);
        return PageView.PURCHASE.getPath();
    }
    
    @PostMapping("/purchase/query")
    public ResponseEntity<?> query(@RequestBody PurchaseDTO rq) {
        PageResult pageResult = purchaseService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/purchase/get/{id}/{companyId}")
    public String get(@PathVariable("id") Long id, @PathVariable("companyId") Long companyId, Model model) {
        List<SizeChart> sizeChartList = sizeChartService.getAll();
        List<Product> productList = productService.findByCompanyId(companyId);
        Purchase purchase = purchaseService.get(id);
        if (purchase != null) {
            companyId = purchase.getCompanyId();
            productList = productService.findByCompanyId(companyId);
        }
        Company company = companyService.get(companyId);
        model.addAttribute("vo", PurchaseMapper.INSTANCE.to(purchase));
        model.addAttribute("sizeChartList", sizeChartList);
        model.addAttribute("company", company);
        model.addAttribute("productList", productList);
        return PageView.PURCHASE.getPath() + "::purchase";
    }
    
    @PostMapping("/purchase/add")
    public ResponseEntity<?> add(@RequestBody PurchaseDTO rq) {
        purchaseService.add(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/purchase/update")
    public ResponseEntity<?> update(@RequestBody PurchaseDTO rq) {
        purchaseService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/purchase/save/buy")
    public ResponseEntity<?> buy(@RequestBody PurchaseDTO rq) {
        String buyNo = purchaseService.buy(rq.getPurchaseDate(), rq.getCompanyOrderNo());
        return new ResponseEntity<>(Result.success(buyNo), HttpStatus.OK);
    }
    
    @DeleteMapping("/purchase/delete/buy")
    public ResponseEntity<?> delBuy() {
        purchaseService.delBuy();
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @DeleteMapping("/purchase/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        purchaseService.del(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/purchase/save/inventory")
    public ResponseEntity<?> saveInventory(@RequestBody PurchaseDTO rq) {
        purchaseService.saveToInventory(rq.getBuyNo());
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
