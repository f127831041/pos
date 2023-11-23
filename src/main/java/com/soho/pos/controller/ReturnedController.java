
package com.soho.pos.controller;

import com.soho.pos.dto.ReturnedDTO;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.Returned;
import com.soho.pos.entity.SizeChart;
import com.soho.pos.enums.PageView;
import com.soho.pos.mapper.ReturnedMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.ProductService;
import com.soho.pos.service.ReturnedService;
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
public class ReturnedController {
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ReturnedService returnedService;
    
    @GetMapping("/returned")
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
        return PageView.RETURNED.getPath();
    }
    
    @PostMapping("/returned/query")
    public ResponseEntity<?> query(@RequestBody ReturnedDTO rq) {
        PageResult pageResult = returnedService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/returned/get/{id}/{companyId}")
    public String get(@PathVariable("id") Long id, @PathVariable("companyId") Long companyId, Model model) {
        List<SizeChart> sizeChartList = sizeChartService.getAll();
        List<Product> productList = productService.findByCompanyId(companyId);
        Returned returned = returnedService.get(id);
        if (returned != null) {
            companyId = returned.getCompanyId();
            productList = productService.findByCompanyId(companyId);
        }
        Company company = companyService.get(companyId);
        model.addAttribute("vo", ReturnedMapper.INSTANCE.to(returned));
        model.addAttribute("sizeChartList", sizeChartList);
        model.addAttribute("company", company);
        model.addAttribute("productList", productList);
        return PageView.RETURNED.getPath() + "::returned";
    }
    
    @PostMapping("/returned/add")
    public ResponseEntity<?> add(@RequestBody ReturnedDTO rq) {
        returnedService.add(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/returned/update")
    public ResponseEntity<?> update(@RequestBody ReturnedDTO rq) {
        returnedService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/returned/saveBack")
    public ResponseEntity<?> saveBack(@RequestBody ReturnedDTO rq) {
        String buyNo = returnedService.saveBack(rq.getReturnedDate(), rq.getCompanyOrderNo());
        return new ResponseEntity<>(Result.success(buyNo), HttpStatus.OK);
    }
    
    @DeleteMapping("/returned/deleteAll")
    public ResponseEntity<?> delBack() {
        returnedService.delBack();
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @DeleteMapping("/returned/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        returnedService.del(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/returned/save/inventory")
    public ResponseEntity<?> saveInventory(@RequestBody ReturnedDTO rq) {
        returnedService.saveToInventory(rq.getBuyNo());
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
