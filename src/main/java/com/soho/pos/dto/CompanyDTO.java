package com.soho.pos.dto;

import com.soho.pos.model.DataTable;
import lombok.Data;

@Data
public class CompanyDTO extends DataTable {
    private Long id;
    private String brand;
    private String salesName;
    private String salesName2;
    private String mobilePhone;
    private String mobilePhone2;
    private String phone;
    private String phone2;
    private String address;
    private String address2;
}
