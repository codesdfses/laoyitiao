package com.laoyitiao.repositories;

import com.laoyitiao.entities.ImGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImGroupRepository extends JpaRepository<ImGroup, String> {

}