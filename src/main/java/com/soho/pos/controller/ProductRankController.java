

package com.soho.pos.controller;

import com.soho.pos.dto.ProductRankDTO;
import com.soho.pos.dto.SaleDTO;
import com.soho.pos.entity.*;
import com.soho.pos.enums.PageView;
import com.soho.pos.mapper.InventoryMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.*;
import com.soho.pos.vo.InventoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class ProductRankController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ProductRankService productRankService;
    
    @GetMapping("/rank")
    public String init(Model model) {
        List<Company> companyList = companyService.getAll("desc", "id");
        List<Product> productList = productService.getAll("desc", "id").stream()
                .filter(x -> "1".equals(x.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("productList", productList);
        model.addAttribute("companyList", companyList);
        return PageView.PRODUCT_RANK.getPath();
    }
    
    @PostMapping("/rank/query")
    public ResponseEntity<?> query(@RequestBody ProductRankDTO rq) {
        PageResult pageResult = productRankService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
}
