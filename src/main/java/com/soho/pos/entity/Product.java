package com.soho.pos.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author viper
 * @Date 2023/4/27 下午 02:43
 * @Version 1.0
 */
@Entity
@Table(name = "product")
@Data
public class Product extends BaseEntity {
    
    /**
     * 品牌id
     */
    @Column(name = "companyId", columnDefinition = "BIGINT COMMENT '品牌id'")
    private Long companyId;
    
    /**
     * 型號
     */
    @Column(name = "prod_no", length = 100, columnDefinition = "varchar(100) COMMENT '型號'")
    private String prodNo;
    
    /**
     * 品項
     */
    @Column(name = "name", length = 100, columnDefinition = "varchar(100) COMMENT '品項'")
    private String name;
    
    /**
     * 顏色
     */
    @Column(name = "color", length = 20, columnDefinition = "varchar(20)  COMMENT '顏色'")
    private String color;
    
    /**
     * 圖案
     */
    @Column(name = "design", length = 20, columnDefinition = "varchar(20)  COMMENT '圖案'")
    private String design;
    
    /**
     * 進貨價格
     */
    @Column(name = "purchase_price", length = 11, columnDefinition = "int(11)  COMMENT '進貨價格'")
    private Integer purchasePrice;
    
    /**
     * 販售價格
     */
    @Column(name = "price", length = 11, columnDefinition = "int(11)  COMMENT '販售價格'")
    private Integer price;
    
    /**
     * 總金額
     */
    @Column(name = "pay", length = 11, columnDefinition = "int(11)  COMMENT '總金額'")
    private Integer pay;
    
    /**
     * 狀態 0:下架 1:上架
     */
    @Column(name = "status", length = 1, columnDefinition = "varchar(1) DEFAULT '1' COMMENT '狀態 0:下架 1:上架'")
    private String status;
    
}
