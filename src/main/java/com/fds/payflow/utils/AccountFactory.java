package com.fds.payflow.utils;

import com.fds.payflow.vo.Account;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class AccountFactory {
    public Account createAccount(Member member){
        if (member.getUserId().equals("user001")){
            return new Account("aaabbb", member.getUserId() + "의 통장", 100000L, member);
        }
        return new Account(generateAccountNumber(), member.getUserId() + "의 통장", 100000L, member);
    }

    private String generateAccountNumber(){
        return UUID.randomUUID().toString().replace("-", "").substring(0,6);
    }
}
