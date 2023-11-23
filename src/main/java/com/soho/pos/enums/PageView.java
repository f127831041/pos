package com.soho.pos.enums;

import lombok.Getter;

@Getter
public enum PageView {
    /*後台*/
    LOGIN("login"),
    HOME("home"),
    COMPANY_PAGE("company"),
    MEMBER("member"),
    PRODUCT("product"),
    INVENTORY("inventory"),
    PURCHASE_HISTORY("purchaseHistory"),
    RETURNED_HISTORY("returnedHistory"),
    ACTIVITY("activity"),
    COMPANY_REPORT("companyReport"),
    SHOP_REPORT("shopReport"),
    REFUND("refund"),
    COST_REPORT("costReport"),
    SALE("sale"),
    PRODUCT_RANK("rank"),
    REVENUE("revenue"),
    REFUND_RECORD("refundRecord"),
    PURCHASE("purchase"),
    RETURNED("returned"),
    MANAGER("manager"),
    ERROR("error");
    
    private final String path;
    
    PageView(String path) {
        this.path = path;
    }
    
}
