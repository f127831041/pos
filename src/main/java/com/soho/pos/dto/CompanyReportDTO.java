package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class CompanyReportDTO extends DataTable {
    private String date;
    private Long companyId;
}
