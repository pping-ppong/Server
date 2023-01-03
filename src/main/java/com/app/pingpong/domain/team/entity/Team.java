package com.app.pingpong.domain.team.entity;

import com.app.pingpong.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name="TEAMS")
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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<UserTeam> members = new ArrayList<>();

    @Builder
    public Team(String name) {
        this.name = name;
    }

    public void setHost(User host) {
        this.host = host;
    }
}


