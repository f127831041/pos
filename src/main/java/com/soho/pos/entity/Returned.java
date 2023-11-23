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
@Table(name = "returned")
@Data
public class Returned extends BaseEntity {
    
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
     * 型號
     */
    @Column(name = "prod_no", length = 100, columnDefinition = "varchar(100) COMMENT '型號'")
    private String prodNo;
    
    /**
     * 品牌id
     */
    @Column(name = "company_id", columnDefinition = "BIGINT COMMENT '品牌id'")
    private Long companyId;
    
    /**
     * 進貨價格
     */
    @Column(name = "price", length = 11, columnDefinition = "int(11)  COMMENT '進貨價格'")
    private Integer price;
    
    /**
     * 稅率
     */
    @Column(name = "tariff", columnDefinition = "int(11) COMMENT '稅率'")
    private Integer tariff;
    
}
