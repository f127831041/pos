package com.soho.pos.enums;

import java.util.Arrays;
import java.util.List;

public enum ErrorText {
    SYSTEM_FAIL("執行此操作時發生錯誤！請測試相關環境或洽詢服務廠商。"),
    USER_INFO_FAIL("無法取得登入者資訊，請重新登入！"),
    LOGIN_FAIL("帳密錯誤！"),
    LOGIN_STATUS_FAIL("帳號已停用！"),
    ID_FAIL("%s ID錯誤！"),
    PRODUCT_FAIL("產品已有建立紀錄！"),
    SIZE_FAIL("尺寸設定有誤！"),
    INVENTORY_FAIL("%s尚有庫存，無法刪除型錄！"),
    EXEC_INVENTORY_FAIL("%s退貨超出庫存數量！"),
    EMPTY_INVENTORY_FAIL("無庫存數量可退貨！"),
    SIZE_EXISTS_FAIL("該商品尺寸已存在，無法重複建立！"),
    COMPANY_EXISTS_FAIL("品牌已存在，無法重複建立！");
//    FILE_SIZE_FAIL("%s超出檔案最大限制！"),
//    FILE_MIME_FAIL("%s檔案類型錯誤！"),
//    FILE_NAME_FAIL("%s檔案名稱與原檔不匹配！"),
//    FILE_EMPTY_FAIL("未上傳%s檔案！"),
//    NUMERIC_FAIL("%s數值填寫錯誤！"),
//    EXIST_FAIL("已存在相同%s！"),
//    DATE_FAIL("日期區間錯誤！"),
//    TIME_FAIL("%s時間區間錯誤！"),
//    FIELD_FAIL("%s必須輸入！"),
//    ACTIVITY_FAIL("該活動項目紀錄已存在！"),
//    LIB_OPEN_FAIL("日期區間已有開閉館紀錄！"),

//    API_KEY_FAIL("token驗證錯誤！"),
//    PAGE_LANG_FAIL("已有%s語系頁面！"),
//    FILE_STATUS_FAIL("檔案已在使用中！"),
//    VERSION_SIZE_FAIL("檔案版本已達上限！"),
//    SIZE_FAIL("%s已達上限！"),
//    FILE_USE_FAIL("檔案啟用已達上限！"),
//    HTTP_FAIL("您填入的連結不符合http(s)的規範，可能會導致連線或系統異常！");
    
    private String str;
    
    ErrorText(String str) {
        this.str = str;
    }
    
    public String getMsg(String... context) {
        if (context.length > 0) {
            // 找出有多少個%s
            int signCount = str.split("%s").length - 1;
            int supplementCount = signCount - context.length;
            // 傳入的參數個數不足，需產生新陣列，長度必須至少跟%s個數一樣(否則format會出錯)，並將null替換成""
            if (supplementCount > 0) {
                List<String> al = Arrays.asList(Arrays.copyOf(context, signCount));
                Object[] newContext = al.stream().map(s -> (s == null ? "" : s)).toArray();
                return String.format(str, newContext);
            }
            
            return String.format(str, context);
        } else {
            str = str.replaceAll("%s", "");
            return str;
        }
    }
}
