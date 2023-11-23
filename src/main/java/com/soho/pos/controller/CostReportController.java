

package com.soho.pos.controller;

import com.soho.pos.dto.CompanyReportDTO;
import com.soho.pos.dto.CostReportDTO;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.CompanyReportService;
import com.soho.pos.service.CompanyService;
import com.soho.pos.service.CostReportService;
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
public class CostReportController {
    @Autowired
    private CostReportService reportService;
    @Autowired
    private CompanyService companyService;
    
    @GetMapping("/costReport")
    public String init(Model model) {
        model.addAttribute("companyList", companyService.getAll("desc", "id"));
        return PageView.COST_REPORT.getPath();
    }
    
    @PostMapping("/costReport/query")
    public ResponseEntity<?> query(@RequestBody CostReportDTO rq) {
        PageResult pageResult = reportService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
}
