package com.blog.post.controller;
//src/main/java/com/example/demo/controller/UserController.java




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.blog.post.entity.Post;
import com.blog.post.entity.User;
import com.blog.post.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
 @Autowired
 private UserService userService;

 @GetMapping
 public List<User> getAllUsers() {
     return userService.getAllUsers();
 }

 @GetMapping("/{id}")
 public User getUserById(@PathVariable Long id) {
     return userService.getUserById(id);
 }
 @Autowired
 ObjectMapper objectMapper;

 @PostMapping
 public User saveUser(@RequestBody String user) throws JsonMappingException, JsonProcessingException {
	  User users = objectMapper.readValue(user, User.class);
     return userService.saveUser(users);
 }
}

