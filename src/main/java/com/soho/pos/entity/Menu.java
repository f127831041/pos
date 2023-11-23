package com.soho.pos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author viper
 * @Date 2023/4/28 上午 11:15
 * @Version 1.0
 */
@Entity
@Table(name = "menu")
@Data
public class Menu extends BaseEntity {
    
    /**
     * 功能名稱
     */
    @Column(name = "name", length = 100, columnDefinition = "varchar(100) COMMENT '功能名稱'")
    private String name;
    
    /**
     * 功能頁面
     */
    @Column(name = "page", length = 100, columnDefinition = "varchar(100) COMMENT '頁面'")
    private String page;
    
    /**
     * 順序
     */
    @Column(name = "seq", nullable = false, columnDefinition = "int COMMENT '順序'")
    private Integer seq;
    
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    
}
