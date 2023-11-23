package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class ManagerDTO extends DataTable {
    private Long id;
    private String account;
    private String password;
    private String cname;
    private String status;
    private String phone;
}
