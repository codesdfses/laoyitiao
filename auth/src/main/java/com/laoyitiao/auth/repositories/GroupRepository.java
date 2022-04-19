package com.laoyitiao.auth.repositories;

import com.laoyitiao.auth.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}