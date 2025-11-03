package com.fds.payflow.vo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Membership membership;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Account> accounts;

    public static class builder {
        String userId;
        String password;
        Membership membership = Membership.BASIC;

        public builder userId(String userId){
            this.userId = userId;
            return this;
        }

        public builder password(String password){
            this.password = password;
            return this;
        }

        public builder membership(Membership membership) {
            this.membership = membership;
            return this;
        }

        public Member build(){
            return new Member(this);
        }
    }

    private Member(builder builder){
        this.userId = builder.userId;
        this.password = builder.password;
        this.membership = builder.membership;
    }

    public void setAccounts(Account... paramAccounts) {
        if (accounts == null || accounts.isEmpty()) {
            this.accounts = new ArrayList<>();
            Collections.addAll(this.accounts, paramAccounts);
        }else{
            Collections.addAll(this.accounts, paramAccounts);
        }
    }

    public Collection<GrantedAuthority> getAuthorities(){
        return Collections.singletonList(new SimpleGrantedAuthority(membership.toString()));
    }
}
