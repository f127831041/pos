package com.soho.pos.vo;

import com.soho.pos.entity.Company;
import lombok.Data;

/**
 * @Author viper
 * @Date 2023/5/8 下午 08:14
 * @Version 1.0
 */
@Data
public class PurchaseHistoryVO {
    private long id;
    private String buyNo;
    private String status;
    private String purchaseDate;
    private int num;
    private Company company;
    private String companyOrderNo;
}
