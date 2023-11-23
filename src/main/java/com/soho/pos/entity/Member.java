package com.soho.pos.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author viper
 * @Date 2023/4/27 下午 02:43
 * @Version 1.0
 */
@Entity
@Table(name = "member")
@Data
public class Member extends BaseEntity {
    
    /**
     * 生日(月)
     */
    @Column(name = "month", length = 5, columnDefinition = "varchar(5) COMMENT '生日(月)'")
    private String month;
    
    /**
     * 生日(日)
     */
    @Column(name = "day", length = 5, columnDefinition = "varchar(5) COMMENT '生日(日)'")
    private String day;
    
    /**
     * 中文姓名
     */
    @Column(name = "cname", length = 20, columnDefinition = "varchar(20) COMMENT '中文姓名'")
    private String cname;
    
    /**
     * 電話
     */
    @Column(name = "phone", length = 20, columnDefinition = "varchar(20)  COMMENT '電話'")
    private String phone;
    
    /**
     * 紅利點數
     */
    @Column(name = "points", columnDefinition = "int(11)  COMMENT '紅利點數'")
    private Integer points;
    
}
