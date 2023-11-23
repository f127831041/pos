

package com.soho.pos.controller;

import com.soho.pos.dto.RevenueDTO;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class RevenueController {
    @Autowired
    private RevenueService revenueService;
    
    @GetMapping("/revenue")
    public String init() {
        return PageView.REVENUE.getPath();
    }
    
    @PostMapping("/revenue/query")
    public ResponseEntity<?> query(@RequestBody RevenueDTO rq) {
        PageResult pageResult = revenueService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    
    
}
