package com.soho.pos.vo;

import com.soho.pos.entity.Company;
import lombok.Data;

/**
 * @Author viper
 * @Date 2023/5/8 下午 08:14
 * @Version 1.0
 */
@Data
public class CostReportVO {
    private Company company;
    private int purchase;
    private int returned;
    private int total;
}
