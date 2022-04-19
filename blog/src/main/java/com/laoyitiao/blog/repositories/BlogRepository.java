package com.laoyitiao.blog.repositories;

import com.laoyitiao.blog.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}