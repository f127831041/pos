package com.soho.pos.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author viper
 * @Date 2023/4/27 下午 02:43
 * @Version 1.0
 */
@Entity
@Table(name = "purchase")
@Data
public class Purchase extends BaseEntity {
    
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
     * 稅率
     */
    @Column(name = "tariff", columnDefinition = "int(11) COMMENT '稅率'")
    private Integer tariff;
    
    /**
     * 進貨價格
     */
    @Column(name = "price", length = 11, columnDefinition = "int(11)  COMMENT '進貨價格'")
    private Integer price;
    
}
