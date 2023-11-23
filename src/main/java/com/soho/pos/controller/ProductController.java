
package com.soho.pos.controller;

import com.soho.pos.dto.ProductDTO;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Product;
import com.soho.pos.enums.PageView;
import com.soho.pos.mapper.ProductMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.ProductService;
import com.soho.pos.service.SizeChartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/product")
    public String init(Model model) {
        model.addAttribute("companyList", companyService.getAll("desc", "id"));
        return PageView.PRODUCT.getPath();
    }
    
    @PostMapping("/product/query")
    public ResponseEntity<?> query(@RequestBody ProductDTO rq) {
        PageResult pageResult = productService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/product/get/{id}")
    public String get(@PathVariable("id") Long id, Model model) {
        Product vo = productService.get(id);
        model.addAttribute("companyList", companyService.getAll("desc", "id"));
        model.addAttribute("vo", ProductMapper.INSTANCE.to(vo));
        return PageView.PRODUCT.getPath() + "::product";
    }
    
    @PostMapping("/product/get/data")
    public ResponseEntity<?> getData(@RequestBody ProductDTO rq) {
        List<Product> productList = productService.getData(rq);
        List<String> prodNoList = productList.stream()
                .map(Product::getProdNo)
                .distinct()
                .collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("prodNoList", prodNoList);
        map.put("productList",productList);
        return new ResponseEntity<>(Result.success(map), HttpStatus.OK);
    }
    
    @PostMapping("/product/add")
    public ResponseEntity<?> add(@RequestBody ProductDTO rq) {
        productService.add(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/product/update")
    public ResponseEntity<?> update(@RequestBody ProductDTO rq) {
        productService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        productService.del(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @GetMapping("/product/barcode/{id}")
    public ResponseEntity<?> barcode(@PathVariable("id") Long id) throws Exception {
        productService.barcode(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
