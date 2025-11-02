package com.fds.payflow.service;

import com.fds.payflow.repository.AccountRepository;
import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.utils.AccountFactory;
import com.fds.payflow.vo.Account;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final AccountFactory accountFactory;

    public Member createMember(String userId, String password){
        if (!memberRepository.existsByUserId(userId)) {

            Member build = new Member.builder()
                    .userId(userId)
                    .password(encoder.encode(password))
                    .build();

            Account account = accountFactory.createAccount(build);
            build.setAccounts(account);

            return memberRepository.save(build);
        }else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public boolean login(String userId, String password){
        Member member = memberRepository.findMemberByUserId(userId).orElseThrow(IllegalArgumentException::new);
        return encoder.matches(password, member.getPassword());
    }
}
