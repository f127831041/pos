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
@Table(name = "size_chart")
@Data
public class SizeChart extends BaseEntity {
    
    /**
     * 尺寸
     */
    @Column(name = "size", length = 100, columnDefinition = "varchar(100) COMMENT '尺寸'")
    private String size;
    
}
