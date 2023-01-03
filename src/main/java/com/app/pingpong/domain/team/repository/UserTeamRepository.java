package com.app.pingpong.domain.team.repository;

import com.app.pingpong.domain.team.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTeamRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findAllByTeamId(Long teamId);
    List<TeamMember> findAllByUserId(Long userId);
    TeamMember findByTeamIdAndUserId(Long teamId, Long userId);
}
