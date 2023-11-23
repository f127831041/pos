package com.soho.pos.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * @Author viper
 * @Date 2023/4/28 上午 11:15
 * @Version 1.0
 */
@Entity
@Table(name = "product_rank")
@Data
public class ProductRank extends BaseEntity {
    
    /**
     * 銷售日期
     */
    @Column(name = "sale_date", columnDefinition = "date COMMENT '銷售日期'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    private LocalDate saleDate;
    
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
     * 數量
     */
    @Column(name = "cnt", columnDefinition = "int(11) COMMENT '數量'")
    private Integer cnt;
    
}
