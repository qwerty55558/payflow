package com.fds.payflow.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Feed {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    public Feed(String content) {
        this.content = content;
    }
}
