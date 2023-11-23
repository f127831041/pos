package com.soho.pos.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class PageResult {
    //執行次數
    private int draw;
    //總共頁數
    private int pageCount;
    //資料總筆數
    private int totalCount;
    //資料
    private List data;
    //其他參數
    private Map dataMap;
    
    /**
     * DataTable分頁物件
     *
     * @param rows     資料
     * @param page     起始頁
     * @param pageSize 每頁筆數
     * @param draw     執行次數
     * @param dataMap  其他參數
     * @return
     */
    private PageResult(List rows, int page, int pageSize, int draw, Map dataMap) {
        double total = Math.ceil((double) rows.size() / pageSize);
        this.totalCount = rows.size();
        this.pageCount = ((int) total == 0 ? 1 : (int) total);
        this.data = (List) rows.stream().skip(page).limit(pageSize).collect(Collectors.toList());
        this.draw = draw;
        this.dataMap = dataMap;
    }
    
    public static PageResult getPageResult(List rows, int page, int pageSize, int draw) {
        return new PageResult(rows, page, pageSize, draw, null);
    }
    
    public static PageResult getPageResult(List rows, int page, int pageSize, int draw, Map dataMap) {
        return new PageResult(rows, page, pageSize, draw, dataMap);
    }
}