package com.doobie.trading.repository;

import com.doobie.trading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailId(String emailId);
}
