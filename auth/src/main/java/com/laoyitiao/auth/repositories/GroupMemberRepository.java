package com.laoyitiao.auth.repositories;

import com.laoyitiao.auth.entities.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
}