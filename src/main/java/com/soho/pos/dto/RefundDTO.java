package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class RefundDTO extends DataTable {
    private Long sizeId;
    private int cnt;
    private Long productId;
    private int total;
    private int pay;
    private int bonus;
    private String date;
    private String phone;
    private String payMethod;
}
