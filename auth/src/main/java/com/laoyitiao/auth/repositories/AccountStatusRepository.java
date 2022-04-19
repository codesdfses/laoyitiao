package com.laoyitiao.auth.repositories;

import com.laoyitiao.auth.entities.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStatusRepository extends JpaRepository<AccountStatus, String> {
}