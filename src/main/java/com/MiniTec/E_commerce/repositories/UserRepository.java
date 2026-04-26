package com.MiniTec.E_commerce.repositories;

import com.MiniTec.E_commerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
