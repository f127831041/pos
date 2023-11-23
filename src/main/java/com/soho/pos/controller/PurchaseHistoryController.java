

package com.soho.pos.controller;

import com.soho.pos.dto.PurchaseHistoryDTO;
import com.soho.pos.entity.*;
import com.soho.pos.enums.PageView;
import com.soho.pos.mapper.PurchaseHistoryMapper;
import com.soho.pos.mapper.PurchaseMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class PurchaseHistoryController {
    @Autowired
    private PurchaseHistoryService historyService;
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/purchaseHistory")
    public String init(Model model) {
        model.addAttribute("companyList", companyService.getAll("desc", "id"));
        return PageView.PURCHASE_HISTORY.getPath();
    }
    
    @PostMapping("/purchaseHistory/query")
    public ResponseEntity<?> query(@RequestBody PurchaseHistoryDTO rq) {
        PageResult pageResult = historyService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/purchaseHistory/get/{id}")
    public String get(@PathVariable("id") Long id, Model model) {
        PurchaseHistory purchaseHistory = historyService.get(id);
        model.addAttribute("vo", PurchaseHistoryMapper.INSTANCE.to(purchaseHistory));
        return PageView.PURCHASE_HISTORY.getPath() + "::history";
    }

    @PostMapping("/purchaseHistory/save/inventory")
    public ResponseEntity<?> saveInventory(@RequestBody PurchaseHistoryDTO rq) {
        historyService.saveToInventory(rq.getBuyNo());
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/purchaseHistory/update")
    public ResponseEntity<?> upd(@RequestBody PurchaseHistoryDTO rq) {
        historyService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }

    @PostMapping("/purchaseHistory/data/query")
    public ResponseEntity<?> dataInfo(@RequestBody PurchaseHistoryDTO rq) {
        PageResult pageResult = historyService.findByDataInfo(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @DeleteMapping("/purchaseHistory/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        historyService.del(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
