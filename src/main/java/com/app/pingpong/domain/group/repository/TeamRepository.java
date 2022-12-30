package com.app.pingpong.domain.group.repository;

import com.app.pingpong.domain.group.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
