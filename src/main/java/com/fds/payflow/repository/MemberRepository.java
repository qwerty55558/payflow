package com.fds.payflow.repository;

import com.fds.payflow.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
