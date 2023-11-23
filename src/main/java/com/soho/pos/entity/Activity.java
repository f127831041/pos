package com.soho.pos.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author viper
 * @Date 2023/4/28 上午 11:15
 * @Version 1.0
 */
@Entity
@Table(name = "activity")
@Data
public class Activity extends BaseEntity {
    
    /**
     * 紅利-消費金額
     */
    @Column(name = "bonus_money", columnDefinition = "int(11) COMMENT '紅利-消費金額'")
    private Integer bonusMoney;
    
    /**
     * 紅利-是否啟用 1是:0否
     */
    @Column(name = "bonus_type",  columnDefinition = "varchar(1) DEFAULT '0' COMMENT '紅利-是否啟用 1是:0否'")
    private String bonusType;
    
    /**
     * 紅利-轉換
     */
    @Column(name = "bonus_convert", columnDefinition = "int(11) COMMENT '紅利-轉換'")
    private Integer bonusConvert;
    
    /**
     * 折扣-消費金額
     */
    @Column(name = "discount_money", columnDefinition = "int(11) COMMENT '折扣-消費金額'")
    private Integer discountMoney;
    
    /**
     * 折扣-是否啟用 1是:0否
     */
    @Column(name = "discount_type",  columnDefinition = "varchar(1) DEFAULT '0' COMMENT '折扣-是否啟用 1是:0否'")
    private String discountType;
    
    /**
     * 折扣-轉換
     */
    @Column(name = "discount_convert", columnDefinition = "int(11) COMMENT '折扣-轉換'")
    private Integer discountConvert;
    
}
