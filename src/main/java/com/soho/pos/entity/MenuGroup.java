package com.soho.pos.entity;

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
@Table(name = "menu_group")
@Data
public class MenuGroup extends BaseEntity {
    
    /**
     * 群組名稱
     */
    @Column(name = "name", length = 100, columnDefinition = "varchar(100) COMMENT '群組名稱'")
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
    
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "menuGroup" ,fetch=FetchType.EAGER)
    private List<Menu> menuList;
    
}
