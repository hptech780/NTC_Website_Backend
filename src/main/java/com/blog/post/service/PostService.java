package com.blog.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.blog.post.entity.Comment;
import com.blog.post.entity.Like;
import com.blog.post.entity.Post;
import com.blog.post.entity.User;
import com.blog.post.repositories.CommentRepository;
import com.blog.post.repositories.LikeRepository;
import com.blog.post.repositories.PostRepository;
import com.blog.post.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
	private static final String UPLOAD_DIR = "/uploads"; // Directory inside static folder


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;


public List<Post> getAllPosts() {
    List<Post> posts = postRepository.findAll();
    posts.forEach(this::setPhotoURL);
    return posts;
}

public Optional<Post> getPostById(Long id) {
    Optional<Post> post = postRepository.findById(id);
    post.ifPresent(this::setPhotoURL);
    return post;
}

private void setPhotoURL(Post post) {
    if (post.getPhotoPath() != null) {
        String photoUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                          .path("/uploads/")
                          .path(post.getPhotoPath())
                          .toUriString();
        post.setPhotoPath(photoUrl);
    }
}

    public Post savePost(Post post, MultipartFile photoFile, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();

        String fileName = saveFile(photoFile);
        post.setPhotoPath(fileName);
        post.setUser(user);

        return postRepository.save(post);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    public Post likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        likeRepository.save(like);

        post.getLikes().add(like);
        return postRepository.save(post);
    }

    public boolean commentOnPost(Long postId, Long userId, String text) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setText(text);

        commentRepository.save(comment);

        post.getComments().add(comment);
        postRepository.save(post);
        return true;
    }
    

    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPost(post);
    }
    public boolean deletePostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }
}
