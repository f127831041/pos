

package com.soho.pos.controller;

import com.soho.pos.dto.ReturnedHistoryDTO;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.ReturnedHistoryService;
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
public class ReturnedHistoryController {
    @Autowired
    private ReturnedHistoryService historyService;
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/returnedHistory")
    public String init(Model model) {
        model.addAttribute("companyList", companyService.getAll("desc", "id"));
        return PageView.RETURNED_HISTORY.getPath();
    }
    
    @PostMapping("/returnedHistory/query")
    public ResponseEntity<?> query(@RequestBody ReturnedHistoryDTO rq) {
        PageResult pageResult = historyService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }

    @PostMapping("/returnedHistory/save/inventory")
    public ResponseEntity<?> saveInventory(@RequestBody ReturnedHistoryDTO rq) {
        historyService.saveToInventory(rq.getBuyNo());
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }

    @PostMapping("/returnedHistory/data/query")
    public ResponseEntity<?> dataInfo(@RequestBody ReturnedHistoryDTO rq) {
        PageResult pageResult = historyService.findByDataInfo(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @DeleteMapping("/returnedHistory/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        historyService.del(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
