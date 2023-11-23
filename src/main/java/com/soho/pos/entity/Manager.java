package com.soho.pos.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author viper
 * @Date 2023/4/27 下午 02:43
 * @Version 1.0
 */
@Entity
@Table(name = "manager")
@Data
public class Manager  extends BaseEntity {
    
    /**
     * 帳號
     */
    @Column(name = "account", length = 100, columnDefinition = "varchar(100) COMMENT '帳號'")
    private String account;
    
    /**
     * 密碼(md5)
     */
    @Column(name = "password", length = 100, columnDefinition = "varchar(100) COMMENT '密碼'")
    private String password;
    
    /**
     * 中文姓名
     */
    @Column(name = "cname", length = 20, columnDefinition = "varchar(20) COMMENT '中文姓名'")
    private String cname;
    
    /**
     * 狀態 0:停用 1:啟用
     */
    @Column(name = "status", length = 1, columnDefinition = "varchar(1) DEFAULT '1' COMMENT '狀態 0:停用 1:啟用'")
    private String status;
    
    /**
     * 聯絡電話
     */
    @Column(name = "phone", length = 20, columnDefinition = "varchar(20)  COMMENT '聯絡電話'")
    private String phone;
    
}
