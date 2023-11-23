package com.soho.pos.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author viper
 * @Date 2023/4/27 下午 02:43
 * @Version 1.0
 */
@Entity
@Table(name = "inventory")
@Data
public class Inventory extends BaseEntity {
    
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
     * 型號
     */
    @Column(name = "prod_no", length = 100, columnDefinition = "varchar(100) COMMENT '型號'")
    private String prodNo;
    
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
}
