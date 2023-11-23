package com.soho.pos.service;

import com.soho.pos.dao.MemberDAO;
import com.soho.pos.dto.MemberDTO;
import com.soho.pos.entity.Member;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import com.soho.pos.model.PageResult;
import com.soho.pos.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService extends BaseService<Member, Long, MemberDAO> {
    
    public PageResult findPageQuery(MemberDTO rq) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        
        sql.append(" from Member where 1 = 1 ");
        
        if (StringUtils.isNotBlank(rq.getCname())) {
            sql.append(" and cname like :cname ");
            params.put("cname", "%" + rq.getCname() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getPhone())) {
            sql.append(" and phone like :phone ");
            params.put("phone", "%" + rq.getPhone() + "%");
        }
        
        if (StringUtils.isNotBlank(rq.getMonth())) {
            sql.append(" and month =:month ");
            params.put("month", rq.getMonth());
        }
        
        if (StringUtils.isNotBlank(rq.getDay())) {
            sql.append(" and day =:day ");
            params.put("day", rq.getDay());
        }
        
        if ("birthday".equals(rq.getOrderColumn())) {
            sql.append(" order by ").append(" month ").append(rq.getOrderDir()).append(",").append(" day ").append(rq.getOrderDir());
        } else {
            sql.append(" order by ").append(rq.getOrderColumn()).append(" ").append(rq.getOrderDir());
        }
        
        Query query = em.createQuery(sql.toString());
        SqlUtils.getQueryWithParameters(query, params);
        List<Member> resultList = query.getResultList();
        return PageResult.getPageResult(resultList, rq.getPage(), rq.getPageSize(), rq.getDraw());
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void add(MemberDTO rq) throws PortalException {
        Member member = new Member();
        member.setCname(rq.getCname());
        member.setPhone(rq.getPhone());
        member.setMonth(rq.getMonth());
        member.setDay(rq.getDay());
        member.setPoints(rq.getPoints());
        dao.insert(member);
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(MemberDTO rq) throws PortalException {
        Member member = dao.findById(rq.getId()).orElseThrow(() -> new PortalException(ErrorText.ID_FAIL.getMsg()));
        member.setCname(rq.getCname());
        member.setPhone(rq.getPhone());
        member.setMonth(rq.getMonth());
        member.setDay(rq.getDay());
        member.setPoints(rq.getPoints());
        dao.update(member);
    }
    
    public Member findByPhone(String phone){
        return dao.findByPhone(phone);
    }
}
