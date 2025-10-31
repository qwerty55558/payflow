package com.fds.payflow.service;

import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyUserDetailService implements UserDetailsService {
    MemberRepository rep;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = rep.findMemberByUserId(username).orElseThrow(IllegalStateException::new);

        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
