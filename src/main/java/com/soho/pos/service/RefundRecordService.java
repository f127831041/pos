package com.soho.pos.service;

import com.soho.pos.dao.RefundRecordDAO;
import com.soho.pos.dto.RefundRecordDTO;
import com.soho.pos.entity.RefundRecord;
import com.soho.pos.mapper.RefundRecordMapper;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import com.soho.pos.vo.RefundRecordVO;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundRecordService extends BaseService<RefundRecord, Long, RefundRecordDAO> {
    
    public PageResult findPageQuery(RefundRecordDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from RefundRecord where refundId = :refundId ");
        params.put("refundId", rq.getRefundId());
        
        sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<RefundRecord> resultList = query.getResultList();
        List<RefundRecordVO> voList = RefundRecordMapper.INSTANCE.to(resultList);
        long sum = voList.stream().mapToInt(x -> (x.getCnt() * x.getProduct().getPrice())).sum();
        Map<String, Long> map = new HashMap();
        map.put("total", sum);
        return PageResult.getPageResult(voList, rq.getPage(), rq.getPageSize(), rq.getDraw(), map);
    }
}
