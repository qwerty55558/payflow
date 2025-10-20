package com.fds.payflow.vo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
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
    private Membership membership;

    @OneToMany(mappedBy = "member")
    private List<Account> accounts;

    public static class Builder{
        String userId;
        String password;
        private Membership membership = Membership.BASIC;

        public Builder userId(String userId){
            this.userId = userId;
            return this;
        }

        public Builder password(String password){
            this.password = password;
            return this;
        }

        public Builder membership(Membership membership) {
            this.membership = membership;
            return this;
        }

        public Member build(){
            return new Member(this);
        }
    }

    private Member(Builder builder){
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
}
