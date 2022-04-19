package com.laoyitiao.blog.repositories;

import com.laoyitiao.blog.entities.PageCash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageCashRepository extends JpaRepository<PageCash, Long> {
    PageCash findByPageName(String pageName);
}