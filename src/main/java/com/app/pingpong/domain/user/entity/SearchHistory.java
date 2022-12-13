package com.app.pingpong.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content; //검색 내용

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public SearchHistory(String content, User user) {
        this.content = content;
        this.user = user;
    }
}
