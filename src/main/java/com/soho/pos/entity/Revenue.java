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
@Table(name = "revenue")
@Data
public class Revenue extends BaseEntity {
    
    /**
     * 消費金額
     */
    @Column(name = "total", columnDefinition = "int(11) COMMENT '消費金額'")
    private Integer total;
    
    /**
     * 折扣金額
     */
    @Column(name = "discount", columnDefinition = "int(11) COMMENT '折扣金額'")
    private Integer discount;
    
    /**
     * 紅利抵用金
     */
    @Column(name = "bonus", columnDefinition = "int(11) COMMENT '紅利抵用金'")
    private Integer bonus;
    
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
     * 紅利點數
     */
    @Column(name = "points", columnDefinition = "int(11)  COMMENT '紅利點數'")
    private Integer points;
    
    /**
     * 支付方式 1:現金 2:linePay 3:信用卡
     */
    @Column(name = "pay_method", length = 1, columnDefinition = "varchar(1) DEFAULT '1' COMMENT '支付方式 1:現金 2:linePay 3:信用卡'")
    private String payMethod;
    
    /**
     * 門市優惠
     */
    @Column(name = "promotion", columnDefinition = "int(11)  COMMENT '門市優惠'")
    private Integer promotion;
    
}
