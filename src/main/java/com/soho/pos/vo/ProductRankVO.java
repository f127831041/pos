package com.soho.pos.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.SizeChart;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author viper
 * @Date 2023/5/8 下午 08:14
 * @Version 1.0
 */
@Data
public class ProductRankVO {
    private long id = -1;
    private Product product;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    private LocalDate saleDate;
    private Company company;
    private int cnt;
}
