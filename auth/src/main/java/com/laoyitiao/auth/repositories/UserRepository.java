package com.laoyitiao.auth.repositories;

import com.laoyitiao.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}