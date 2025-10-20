package com.fds.payflow.vo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountName;
    private String accountNumber;
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Account(String accountNumber,String accountName, Long balance, Member member) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = balance;
        this.member = member;
    }
}
