package com.blog.post.service;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.post.entity.User;
import com.blog.post.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
 @Autowired
 private UserRepository userRepository;

 public List<User> getAllUsers() {
     return userRepository.findAll();
 }

 public User getUserById(Long id) {
     return userRepository.findById(id).orElseThrow();
 }

 public User saveUser(User user) {
     return userRepository.save(user);
 }
}
