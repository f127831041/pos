package com.soho.pos.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author viper
 * @Date 2023/4/28 上午 11:15
 * @Version 1.0
 */
@Entity
@Table(name = "refund")
@Data
public class Refund extends BaseEntity {
    
    /**
     * 消費金額
     */
    @Column(name = "total", columnDefinition = "int(11) COMMENT '消費金額'")
    private Integer total;
    
    /**
     * 結帳金額
     */
    @Column(name = "pay", columnDefinition = "int(11) COMMENT '結帳金額'")
    private Integer pay;
    
    /**
     * 會員id
     */
    @Column(name = "member_id", columnDefinition = "BIGINT COMMENT '會員id'")
    private Long memberId;
    
    /**
     * 支付方式 1:現金 2:linePay 3:信用卡
     */
    @Column(name = "pay_method", length = 1, columnDefinition = "varchar(1) DEFAULT '1' COMMENT '支付方式 1:現金 2:linePay 3:信用卡'")
    private String payMethod;
    
    /**
     * 已使用紅利
     */
    @Column(name = "bonus", columnDefinition = "int(11) COMMENT '已使用紅利'")
    private Integer bonus;
    
}
