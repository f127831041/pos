package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class MemberDTO extends DataTable {
    private Long id;
    private String cname;
    private String phone;
    private String month;
    private String day;
    private Integer points;
}
