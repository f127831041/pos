package com.soho.pos.utils;


import com.soho.pos.dao.CompanyDAO;
import com.soho.pos.dao.MemberDAO;
import com.soho.pos.dao.ProductDAO;
import com.soho.pos.dao.SizeChartDAO;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Member;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.SizeChart;

import java.time.LocalDateTime;

public class MapperUtils {
    
    public static String getLocalDate(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate().toString();
    }
    
    public static SizeChart getSizeChart(Long id) {
        id = id == null ? -1L : id;
        SizeChartDAO dao = SpringUtils.getBean(SizeChartDAO.class);
        return dao.findById(id).orElse(new SizeChart());
    }
    
    public static Company getCompany(Long id) {
        id = id == null ? -1L : id;
        CompanyDAO dao = SpringUtils.getBean(CompanyDAO.class);
        return dao.findById(id).orElse(new Company());
    }
    
    public static Product getProduct(Long id) {
        id = id == null ? -1L : id;
        ProductDAO dao = SpringUtils.getBean(ProductDAO.class);
        return dao.findById(id).orElse(new Product());
    }
    
    public static Member getMember(Long id) {
        id = id == null ? -1L : id;
        MemberDAO dao = SpringUtils.getBean(MemberDAO.class);
        return dao.findById(id).orElse(new Member());
    }
}
