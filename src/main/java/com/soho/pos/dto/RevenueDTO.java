package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class RevenueDTO extends DataTable {
    private String date;
    private String phone;
}
