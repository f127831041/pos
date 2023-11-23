package com.soho.pos.controller;

import com.soho.pos.dto.CompanyDTO;
import com.soho.pos.entity.Company;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/company")
    public String init() {
        return PageView.COMPANY_PAGE.getPath();
    }
    
    @GetMapping("/company/get/{id}")
    public String get(@PathVariable("id") Long id, Model model) {
        Company vo = companyService.get(id);
        model.addAttribute("vo", vo);
        return PageView.COMPANY_PAGE.getPath() + "::company";
    }
    
    @PostMapping("/company/query")
    public ResponseEntity<?> query(@RequestBody CompanyDTO rq) {
        PageResult pageResult = companyService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @PostMapping("/company/add")
    public ResponseEntity<?> add(@RequestBody CompanyDTO rq) {
        companyService.add(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/company/update")
    public ResponseEntity<?> update(@RequestBody CompanyDTO rq) {
        companyService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @DeleteMapping("/company/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        companyService.delete(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
