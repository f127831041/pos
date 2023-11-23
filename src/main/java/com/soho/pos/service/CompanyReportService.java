package com.soho.pos.service;

import com.soho.pos.dao.CompanyDAO;
import com.soho.pos.dao.ProductDAO;
import com.soho.pos.dao.SaleDAO;
import com.soho.pos.dto.CompanyReportDTO;
import com.soho.pos.entity.Company;
import com.soho.pos.entity.Product;
import com.soho.pos.entity.Sale;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.CompanyReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CompanyReportService extends BaseService<Sale, Long, SaleDAO> {
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CompanyDAO companyDAO;
    
    public PageResult findPageQuery(CompanyReportDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Sale where 1 = 1 ");
        
        if (StringUtils.isNotBlank(rq.getDate())) {
            String sDate = rq.getDate().split("至")[0].trim();
            String eDate = rq.getDate().split("至")[1].trim();
            sql.append(" and substr(createDateTime, 1, 10) >= :sDate ");
            params.put("sDate", sDate);
            
            sql.append(" and substr(createDateTime, 1, 10) <= :eDate ");
            params.put("eDate", eDate);
        }
        
        if (rq.getCompanyId() != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }
        sql.append(" order by companyId desc");
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Sale> resultList = query.getResultList();
        List<CompanyReportVO> voList = new ArrayList<>();
        if (resultList.size() > 0) {
            Map<Long, Product> map = productDAO.findAll().stream().collect(Collectors.toMap(Product::getId, Function.identity()));
            Map<Long, Integer> groupMap = new HashMap<>();
            for (Sale sale : resultList) {
                int total = groupMap.getOrDefault(sale.getCompanyId(), 0);
                Product product = map.get(sale.getProductId());
                total += sale.getCnt() * product.getPrice();
                groupMap.put(sale.getCompanyId(), total);
            }
            
            Map<Long, Company> companyMap = companyDAO.findAll().stream().collect(Collectors.toMap(Company::getId, Function.identity()));
            for (Map.Entry<Long, Integer> entry : groupMap.entrySet()) {
                Company company = companyMap.get(entry.getKey());
                CompanyReportVO vo = new CompanyReportVO();
                vo.setCompany(company);
                vo.setTotal(entry.getValue());
                voList.add(vo);
            }
            voList = voList.stream().sorted(Comparator.comparing(CompanyReportVO::getTotal).reversed()).collect(Collectors.toList());
        }
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
}
