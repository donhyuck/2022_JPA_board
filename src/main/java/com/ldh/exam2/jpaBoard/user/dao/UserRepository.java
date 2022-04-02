package com.ldh.exam2.jpaBoard.user.dao;

import com.ldh.exam2.jpaBoard.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
