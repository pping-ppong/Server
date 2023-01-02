package com.app.pingpong.domain.team.repository;

import com.app.pingpong.domain.team.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    List<UserTeam> findAllByTeamId(Long teamId);
    List<UserTeam> findAllByUserId(Long userId);
}
