package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //전화번호 찾기
    Member findByTel(String tel);

    //이메일 찾기
    Member findByEmail(String email);
    

}
