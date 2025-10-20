package com.fds.payflow.service;

import com.fds.payflow.repository.AccountRepository;
import com.fds.payflow.repository.TransferRepository;
import com.fds.payflow.vo.Account;
import com.fds.payflow.vo.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public List<Account> findAddressesByMemberUserId(String memberId){
        return accountRepository.findAccountsByMemberUserId(memberId);
    }

    @Transactional
    public Transfer transferFromAccount(String fromAccountNum, String toAccountNum, Long amount) {
        Account fromAccount = accountRepository.findAccountByAccountNumber(fromAccountNum);

        if (fromAccount.getBalance() < amount) {
            return null;
        }

        fromAccount.withdraw(amount);

        Account toAccount = accountRepository.findAccountByAccountNumber(toAccountNum);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transfer transfer = new Transfer(fromAccountNum, toAccountNum, amount);
        transferRepository.save(transfer);

        return transfer;
    }
}
