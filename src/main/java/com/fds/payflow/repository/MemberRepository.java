package com.fds.payflow.repository;

import com.fds.payflow.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserId(String userId);
    Optional<Member> findMemberByUserId(String userId);

    @Query("SELECT m FROM Member m JOIN FETCH m.accounts WHERE m.userId = :userId")
    Member findByUserIdFetching(String userId);

    boolean existsByUserId(String userId);
}
