package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class ReturnedDTO extends DataTable {
    private Long id;
    private Long sizeId;
    private Long companyId;
    private Long productId;
    private Integer cnt;
    private Integer price;
    private String prodNo;
    private String buyNo;
    private Integer tariff;
    private String returnedDate;
    private String companyOrderNo;
}
