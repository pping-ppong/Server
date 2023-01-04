package com.app.pingpong.domain.team.repository;

import com.app.pingpong.domain.team.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    List<UserTeam> findAllByTeamId(Long teamId);
    List<UserTeam> findAllByUserId(Long userId);
}
