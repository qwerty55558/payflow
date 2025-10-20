package com.fds.payflow.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer {
    @Id
    @GeneratedValue
    private Long id;

    private String fromAccount;
    private String toAccount;
    private Long amount;

    private LocalDateTime createdAt;

    public Transfer(String fromAccount, String toAccount, Long amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.createdAt = LocalDateTime.now(Clock.tickSeconds(ZoneId.systemDefault()));
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}
