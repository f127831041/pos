package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class ProductDTO extends DataTable {
    private Long id;
    private Long companyId;
    private String prodNo;
    private String name;
    private String color;
    private String design;
    private Integer price;
    private Integer purchasePrice;
    private String status;
}
