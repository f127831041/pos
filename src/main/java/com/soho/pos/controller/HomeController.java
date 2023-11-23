package com.soho.pos.controller;

import com.soho.pos.enums.PageView;
import com.soho.pos.service.HomeService;
import com.soho.pos.service.RefundService;
import com.soho.pos.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class HomeController {
    @Autowired
    private RevenueService revenueService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private HomeService homeService;
    
    @GetMapping("/home")
    public String init(Model model) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        int todayPay = revenueService.getTodayPay();
        int monthPay = revenueService.getMonthPay();
        int todayRefund = refundService.getTodayRefund();
        int monthRefund = refundService.getMonthRefund();
        model.addAttribute("todayPay", decimalFormat.format(todayPay));
        model.addAttribute("monthPay", decimalFormat.format(monthPay));
        model.addAttribute("todayRefund", decimalFormat.format(todayRefund));
        model.addAttribute("monthRefund", decimalFormat.format(monthRefund));
        model.addAttribute("companyChart", homeService.getCompanyChartData());
        model.addAttribute("revenueChart", homeService.getRevenueChartData());
        return PageView.HOME.getPath();
    }
}
