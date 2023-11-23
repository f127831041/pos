package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class SaleDTO extends DataTable {
    private Long inventoryId;
    private Integer cnt;
    private Integer total;
    private Integer discount;
    private Integer bonus;
    private Integer pay;
    private Integer promotion;
    private Long revenueId;
    private String payMethod;
}
