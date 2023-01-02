package com.app.pingpong.domain.team.entity;

import com.app.pingpong.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private User host;

    @OneToMany(mappedBy = "team")
    private List<UserTeam> members = new ArrayList<>();

    @Builder
    public Team(String name) {
        this.name = name;
    }

    public void setHost(User host) {
        this.host = host;
    }
}


