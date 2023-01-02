package com.app.pingpong.domain.team.repository;

import com.app.pingpong.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
