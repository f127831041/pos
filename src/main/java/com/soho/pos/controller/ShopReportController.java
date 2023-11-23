

package com.soho.pos.controller;

import com.soho.pos.dto.CompanyReportDTO;
import com.soho.pos.dto.ShopReportDTO;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyReportService;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.ShopReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class ShopReportController {
    @Autowired
    private ShopReportService shopReportService;
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/shopReport")
    public String init(Model model) {
        return PageView.SHOP_REPORT.getPath();
    }
    
    @PostMapping("/shopReport/query")
    public ResponseEntity<?> query(@RequestBody ShopReportDTO rq) {
        PageResult pageResult = shopReportService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
}
