package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class PurchaseHistoryDTO extends DataTable {
    private String date;
    private Integer price;
    private Long id;
    private String buyNo;
    private String companyOrderNo;
    private Long companyId;
}
