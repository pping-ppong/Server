package com.app.pingpong.domain.user.entity;

import com.app.pingpong.domain.team.entity.Team;
import com.app.pingpong.domain.team.entity.TeamMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name="USERS")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialIdx;

    private String email;

    private String nickname;

    private String profileImage;

    @Enumerated
    private Authority authority;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder
    public User(String socialIdx, String email, String nickname, String profileImage, Authority authority) {
        this.socialIdx = socialIdx;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.authority = authority;
    }

    public void setTeam(Team team) {
        TeamMember teamMember = new TeamMember(this, team);
        this.teamMembers.add(teamMember);
        team.getMembers().add(teamMember);
    }
}
