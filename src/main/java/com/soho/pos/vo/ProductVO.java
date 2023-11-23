package com.soho.pos.vo;

import com.soho.pos.entity.Company;
import lombok.Data;

/**
 * @Author viper
 * @Date 2023/5/8 下午 08:14
 * @Version 1.0
 */
@Data
public class ProductVO {
    private long id = -1;
    private String brand;
    private String prodNo;
    private String name;
    private String color;
    private String design;
    private int purchasePrice = 1;
    private int price = 1;
    private Company company;
    private String status;
}
