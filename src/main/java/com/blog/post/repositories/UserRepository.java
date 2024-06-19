package com.blog.post.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.post.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	 User findByEmail(String email);
}