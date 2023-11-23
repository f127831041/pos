package com.soho.pos.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 建立者
     */
    @CreatedBy
    @Column(name = "create_user_id", columnDefinition = "bigint(20) COMMENT '建立者'")
    private Long createUserId;

    /**
     * 建立時間
     */
    @CreatedDate
    @Column(name = "create_date_time", columnDefinition = "datetime COMMENT '建立時間'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private LocalDateTime createDateTime;

    /**
     * 更新者
     */
    @LastModifiedBy
    @Column(name = "update_user_id", columnDefinition = "bigint(20) COMMENT '更新者'")
    private Long updateUserId;

    /**
     * 更新時間
     */
    @LastModifiedDate
    @Column(name = "update_date_time", columnDefinition = "datetime COMMENT '更新時間'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private LocalDateTime updateDateTime;

}
