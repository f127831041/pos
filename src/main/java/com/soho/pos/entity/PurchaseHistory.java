package com.soho.pos.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Author viper
 * @Date 2023/4/27 下午 02:43
 * @Version 1.0
 */
@Entity
@Table(name = "purchase_history")
@Data
public class PurchaseHistory extends BaseEntity {
    
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
     * 訂單號
     */
    @Column(name = "buy_no", length = 100, columnDefinition = "varchar(100) COMMENT '訂單號'")
    private String buyNo;
    
    /**
     * 轉入庫存狀態 1:是 2:否
     */
    @Column(name = "status", length = 1, columnDefinition = "varchar(1) DEFAULT '2' COMMENT '轉入庫存狀態 1:是 2:否'")
    private String status;
    
    /**
     * 稅率
     */
    @Column(name = "tariff", columnDefinition = "int(11) COMMENT '稅率'")
    private Integer tariff;
    
    /**
     * 進貨日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    @Column(name = "purchase_date", columnDefinition = "date COMMENT '進貨日期'")
    private LocalDate purchaseDate;
    
    /**
     * 廠商訂單代碼
     */
    @Column(name = "company_order_no", length = 100, columnDefinition = "varchar(100) COMMENT '廠商訂單代碼'")
    private String companyOrderNo;
}
