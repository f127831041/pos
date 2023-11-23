package com.soho.pos.dao;

import com.soho.pos.entity.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDAO extends BaseDAO<Member, Long> {

    Member findByPhone(String phone);
}

