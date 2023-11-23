package com.soho.pos.dto;

import lombok.Data;

@Data
public class ActivityDTO {
    private Long id;
    private Integer bonusMoney;
    private String bonusType;
    private Integer bonusConvert;
    private Integer discountMoney;
    private String discountType;
    private Integer discountConvert;
    
}
