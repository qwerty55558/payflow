package com.fds.payflow.service;

import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.vo.AuthUser;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private final MemberRepository rep;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = rep.findMemberByUserId(username).orElseThrow(IllegalStateException::new);

        return AuthUser.authBuilder()
                .username(member.getUserId())
                .password(member.getPassword())
                .authorities(member.getAuthorities())
                .userId(member.getId())
                .build();
    }
}
