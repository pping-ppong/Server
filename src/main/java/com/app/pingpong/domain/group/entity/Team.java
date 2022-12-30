package com.app.pingpong.domain.group.entity;

import com.app.pingpong.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    //@OneToMany(mappedBy = "group")
    @OneToMany
    private List<User> members = new ArrayList<>();

    @Builder
    public Team(String name) {
        this.name = name;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public List<Long> getTeamUserIdx(Team team) {
        List<Long> teamUserIdx = new ArrayList<>();
        for (User u : team.getMembers()) {
            teamUserIdx.add(u.getId());
        }
        return teamUserIdx;
    }
}


