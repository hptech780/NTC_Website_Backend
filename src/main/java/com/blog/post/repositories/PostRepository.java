package com.blog.post.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
