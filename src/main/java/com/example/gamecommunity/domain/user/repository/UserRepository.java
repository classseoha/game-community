package com.example.gamecommunity.domain.user.repository;

import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);

}
