package com.app.pingpong.domain.user.repository;

import com.app.pingpong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);
    User findBySocialIdx(Long socialIdx);
}
