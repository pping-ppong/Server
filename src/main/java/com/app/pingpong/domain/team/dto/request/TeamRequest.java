package com.app.pingpong.domain.team.dto.request;

import com.app.pingpong.domain.team.entity.Team;
import com.app.pingpong.domain.user.entity.Authority;
import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    private String teamName;
    private List<Long> memberId;

    public Team toEntity() {
        return Team.builder()
                .name(teamName)
                .build();
    }
}
