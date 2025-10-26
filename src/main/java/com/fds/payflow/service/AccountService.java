package com.fds.payflow.service;

import com.fds.payflow.exceptions.EquityAccountException;
import com.fds.payflow.exceptions.NullAccountException;
import com.fds.payflow.exceptions.OutOfBalanceException;
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
    public Transfer transferFromAccount(String fromAccountNum, String toAccountNum, Long amount) throws OutOfBalanceException, NullAccountException, EquityAccountException {
        Account fromAccount = accountRepository.findAccountByAccountNumber(fromAccountNum);

        if (fromAccount.getBalance() < amount) {
            throw new OutOfBalanceException("잔액이 부족합니다.");
        }

        fromAccount.withdraw(amount);

        Account toAccount = accountRepository.findAccountByAccountNumber(toAccountNum);
        if (toAccount == null) {
            throw new NullAccountException("존재하지 않는 계좌입니다");
        }
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new EquityAccountException("같은 계좌로 송금하실 수 없습니다.");
        }
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transfer transfer = new Transfer(fromAccountNum, toAccountNum, amount);
        transferRepository.save(transfer);

        return transfer;
    }
}
