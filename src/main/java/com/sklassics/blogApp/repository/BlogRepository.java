package com.sklassics.blogApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sklassics.blogApp.entity.Blog;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByAuthorId(Long authorId);
}
