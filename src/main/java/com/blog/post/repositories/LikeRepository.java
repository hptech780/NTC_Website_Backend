package com.blog.post.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.post.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}