

package com.soho.pos.controller;

import com.soho.pos.dto.*;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.SizeChart;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class RefundController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private RefundService refundService;
    
    @GetMapping("/refund")
    public String init(Model model) {
        List<Company> companyList = companyService.getAll("desc", "id");
        List<Product> productList = productService.getAll("desc", "id").stream()
                .filter(x -> "1".equals(x.getStatus()))
                .collect(Collectors.toList());
        List<SizeChart> sizeChartList = sizeChartService.getAll();
        model.addAttribute("companyList", companyList);
        model.addAttribute("productList", productList);
        model.addAttribute("sizeChartList", sizeChartList);
        return PageView.REFUND.getPath();
    }
    
    @PostMapping("/refund/query")
    public ResponseEntity<?> query(@RequestBody RefundDTO rq) {
        PageResult pageResult = refundService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @PostMapping("/refund/save")
    public ResponseEntity<?> save(@RequestBody List<RefundDTO> dtoList, HttpSession session) {
        refundService.save(dtoList,session);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
}
