package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class RefundRecordDTO extends DataTable {
    private Long refundId;
}
