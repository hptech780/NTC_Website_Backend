package com.blog.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.blog.post.entity.Comment;
import com.blog.post.entity.Post;
import com.blog.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    // Get all posts
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Get post by ID
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id).orElseThrow();
    }

    // Save a post
    @PostMapping("/{userId}")
    public ResponseEntity<String> createPost(@RequestPart("post") String posts,
                                             @RequestPart("photoFile") MultipartFile photoFile,
                                             @PathVariable Long userId) {
        try {
            Post post = objectMapper.readValue(posts, Post.class);
            Post savedPost = postService.savePost(post, photoFile, userId);
            return ResponseEntity.ok("Post created successfully with ID: " + savedPost.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving post");
        }
    }

    // Like a post
    @PostMapping("/{postId}/like/{userId}")
    public Post likePost(@PathVariable Long postId, @PathVariable Long userId) {
        return postService.likePost(postId, userId);
    }

    // Comment on a post
    @PostMapping("/{postId}/comment/{userId}")
    public boolean commentOnPost(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Map<String, String> body) {
        return postService.commentOnPost(postId, userId, body.get("text"));
    }
    
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = postService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // Delete a post by ID
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable Long postId) {
        boolean isRemoved = postService.deletePostById(postId);
        if (!isRemoved) {
            return ResponseEntity.status(404).body("Post not found with ID: " + postId);
        }
        return ResponseEntity.ok("Post deleted successfully with ID: " + postId);
    }

    
    
}
