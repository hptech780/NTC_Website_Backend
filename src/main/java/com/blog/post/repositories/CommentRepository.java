package com.blog.post.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.post.entity.Comment;
import com.blog.post.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPost(Post post);
}