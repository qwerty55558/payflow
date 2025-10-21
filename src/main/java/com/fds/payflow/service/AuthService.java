package com.fds.payflow.service;

import com.fds.payflow.repository.AccountRepository;
import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.utils.AccountFactory;
import com.fds.payflow.utils.SimpleEncoder;
import com.fds.payflow.vo.Account;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final SimpleEncoder SimpleEncoder;
    private final AccountFactory accountFactory;
    private final AccountRepository accountRepository;

    @Transactional
    public Member createMember(String userId, String password){
        if (!memberRepository.existsByUserId(userId)) {

            Member build = new Member.Builder()
                    .userId(userId)
                    .password(SimpleEncoder.encode(password))
                    .build();

            Account account = accountFactory.createAccount(build);
            build.setAccounts(account);

            return memberRepository.save(build);
        }else {
            if (login(userId, password)) {
                return memberRepository.findByUserId(userId).stream().findAny().orElseThrow();
            } else {
                return null;
            }
        }
    }

    public boolean login(String userId, String password){
        Member member = memberRepository.findByUserId(userId).stream().findAny().orElseThrow();
        return SimpleEncoder.matches(password, member.getPassword());
    }
}
