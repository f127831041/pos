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
@Table(name = "refund_record")
@Data
public class RefundRecord extends BaseEntity {
    
    /**
     * 尺寸id
     */
    @Column(name = "size_id", columnDefinition = "BIGINT COMMENT '尺寸id'")
    private Long sizeId;
    
    /**
     * 數量
     */
    @Column(name = "cnt", columnDefinition = "int(11) COMMENT '數量'")
    private Integer cnt;
    
    /**
     * 產品型錄id
     */
    @Column(name = "product_id", columnDefinition = "BIGINT COMMENT '產品型錄id'")
    private Long productId;
    
    /**
     * 品牌id
     */
    @Column(name = "company_id", columnDefinition = "BIGINT COMMENT '品牌id'")
    private Long companyId;
    
    /**
     * 退貨Id
     */
    @Column(name = "refund_id", columnDefinition = "BIGINT COMMENT '退貨Id'")
    private Long refundId;
}
