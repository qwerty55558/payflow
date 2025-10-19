package com.fds.payflow.vo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "member")
    private List<Account> accounts;

    public static class Builder{
        String userId;
        String password;

        public Builder userId(String userId){
            this.userId = userId;
            return this;
        }

        public Builder password(String password){
            this.password = password;
            return this;
        }

        public Member build(){
            return new Member(this);
        }
    }

    private Member(Builder builder){
        this.userId = builder.userId;
        this.password = builder.password;
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
