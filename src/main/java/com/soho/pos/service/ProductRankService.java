package com.soho.pos.service;

import com.soho.pos.dao.ProductRankDAO;
import com.soho.pos.dto.ProductRankDTO;
import com.soho.pos.entity.ProductRank;
import com.soho.pos.mapper.ProductRankMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.ProductRankVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductRankService extends BaseService<ProductRank, Long, ProductRankDAO> {
    
    public PageResult findPageQuery(ProductRankDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" from ProductRank where 1 = 1 ");
    
        if (StringUtils.isNotBlank(rq.getDate())) {
            String sDate = rq.getDate().split("至")[0].trim();
            String eDate = rq.getDate().split("至")[1].trim();
            sql.append(" and substr(saleDate, 1, 10) >= :sDate ");
            params.put("sDate", sDate);
        
            sql.append(" and substr(saleDate, 1, 10) <= :eDate ");
            params.put("eDate", eDate);
        }

        if (rq.getCompanyId() != null) {
            sql.append(" and companyId = :companyId ");
            params.put("companyId", rq.getCompanyId());
        }

        if (rq.getProductId() != null) {
            sql.append(" and productId = :productId ");
            params.put("productId", rq.getProductId());
        }
        
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());

        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<ProductRank> resultList = query.getResultList();
        List<ProductRankVO> voList = ProductRankMapper.INSTANCE.to(resultList);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
}
