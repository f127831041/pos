package com.soho.pos.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.SizeChart;
import lombok.Data;

/**
 * @Author viper
 * @Date 2023/5/8 下午 08:14
 * @Version 1.0
 */
@Data
public class ShopReportVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    private String createDate;
    private int discount;
    private int bonus;
    private int promotion;
    private int revenueTotal;
    private int revenuePay;
    private int refundTotal;
    private int refundPay;
    private int cash;
    private int line;
    private int card;
    private int pay;
}
