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
public class RefundRecordVO {
    private long id = -1;
    private SizeChart sizeChart;
    private Company company;
    private Product product;
    private int cnt;
}
