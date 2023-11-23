package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class InventoryDTO extends DataTable {
    private Long id;
    private Long sizeId;
    private Long companyId;
    private Long productId;
    private String prodNo;
    private Integer cnt;
}
