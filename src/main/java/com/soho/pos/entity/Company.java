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
@Table(name = "company")
@Data
public class Company extends BaseEntity {
    
    /**
     * 品牌名稱
     */
    @Column(name = "brand", length = 100, columnDefinition = "varchar(100) COMMENT '品牌名稱'")
    private String brand;
    
    /**
     * 業務姓名1
     */
    @Column(name = "sales_name1", length = 100, columnDefinition = "varchar(100) COMMENT '業務姓名1'")
    private String salesName1;
    
    /**
     * 業務姓名2
     */
    @Column(name = "sales_name2", length = 100, columnDefinition = "varchar(100) COMMENT '業務姓名2'")
    private String salesName2;
    
    /**
     * 行動電話1
     */
    @Column(name = "mobile_phone1", length = 100, columnDefinition = "varchar(100) COMMENT '行動電話1'")
    private String mobilePhone1;
    
    /**
     * 行動電話2
     */
    @Column(name = "mobile_phone2", length = 100, columnDefinition = "varchar(100) COMMENT '行動電話2'")
    private String mobilePhone2;
    
    /**
     * 市話1
     */
    @Column(name = "phone1", length = 100, columnDefinition = "varchar(100) COMMENT '市話1'")
    private String phone1;
    
    /**
     * 市話2
     */
    @Column(name = "phone2", length = 100, columnDefinition = "varchar(100) COMMENT '市話2'")
    private String phone2;
    
    /**
     * 地址1
     */
    @Column(name = "address1", length = 200, columnDefinition = "varchar(200) COMMENT '地址1'")
    private String address1;
    
    /**
     * 地址2
     */
    @Column(name = "address2", length = 200, columnDefinition = "varchar(200) COMMENT '地址2'")
    private String address2;
    
}
