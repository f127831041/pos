package com.soho.pos.model;

import lombok.Data;

@Data
public class DataTable {
    //執行次數
    private Integer draw;
    //頁碼
    private Integer page;
    //每頁筆數
    private Integer pageSize;
    //排序欄位
    private String orderColumn;
    //升冪/降冪
    private String orderDir;
}
