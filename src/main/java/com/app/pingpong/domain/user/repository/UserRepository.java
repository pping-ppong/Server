package com.app.pingpong.domain.user.repository;

import com.app.pingpong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
    boolean existsUserByNickname(String nickname);

    User findBySocialIdx(Long socialIdx);

    Optional<User> findByEmail(String email);
    Optional<List<User>> findByNicknameContains(String nickname);
    //List<User> findByUserIdList(List<Long> userIdList);

}
