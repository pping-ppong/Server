package com.app.pingpong.domain.user.repository;

import com.app.pingpong.domain.user.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    SearchHistory findByUserId(Long userIdx);
}
