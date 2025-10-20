package com.fds.payflow.service;

import com.fds.payflow.repository.AccountRepository;
import com.fds.payflow.vo.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> findAddressesByMemberUserId(String memberId){
        return accountRepository.findAccountsByMemberUserId(memberId);
    }
}
