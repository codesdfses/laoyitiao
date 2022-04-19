package com.laoyitiao.auth.repositories;

import com.laoyitiao.auth.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    UserAccount findByAccountNickname(String nickname);
}