package com.soho.pos.vo;

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
public class ReturnedVO {
    private long id = -1;
    private Product product;
    private SizeChart sizeChart;
    private Company company;
    private String prodNo;
    private int cnt;
    private int price;
    private int tariff;
}
