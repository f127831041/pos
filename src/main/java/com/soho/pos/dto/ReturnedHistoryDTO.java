package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class ReturnedHistoryDTO extends DataTable {
    private String date;
    private String buyNo;
    private String companyOrderNo;
    private Long companyId;
}
