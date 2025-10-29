package com.fds.payflow.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Feed {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private String record;
    private Long amount;
    private String img;
    private LocalDateTime recordTime;

    private String senderUserId;
    @Enumerated(EnumType.STRING)
    private Membership senderMembership;

    private String receiverUserId;
    @Enumerated(EnumType.STRING)
    private Membership receiverMembership;

    public Feed(String content, String record, Long amount, String img, LocalDateTime recordTime,
                String senderUserId, Membership senderMembership,
                String receiverUserId, Membership receiverMembership) {
        this.content = content;
        this.record = record;
        this.amount = amount;
        this.img = img;
        this.recordTime = recordTime;
        this.senderUserId = senderUserId;
        this.senderMembership = senderMembership;
        this.receiverUserId = receiverUserId;
        this.receiverMembership = receiverMembership;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", record='" + record + '\'' +
                ", amount=" + amount +
                ", img='" + img + '\'' +
                ", recordTime=" + recordTime +
                ", senderUserId='" + senderUserId + '\'' +
                ", senderMembership=" + senderMembership +
                ", receiverUserId='" + receiverUserId + '\'' +
                ", receiverMembership=" + receiverMembership +
                '}';
    }
}