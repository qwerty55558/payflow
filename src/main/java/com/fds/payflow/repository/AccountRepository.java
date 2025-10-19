package com.fds.payflow.repository;

import com.fds.payflow.vo.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.member.userId = :memberUserId")
    List<Account> findAccountsByMemberUserId(String memberUserId);
}
