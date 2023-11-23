package com.soho.pos.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soho.pos.entity.Member;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author viper
 * @Date 2023/5/8 下午 08:14
 * @Version 1.0
 */
@Data
public class RefundVO {
    private long id = -1;
    private int total;
    private int pay;
    private int bonus;
    private String payMethod;
    private Member member;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    private String createDate;
    private LocalDateTime createDateTime;
}
