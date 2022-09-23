package com.vweinert.fedditbackend.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.payload.request.PostRequest;
import com.vweinert.fedditbackend.payload.response.PostResponse;
import com.vweinert.fedditbackend.repository.PostRepository;
import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostRepository postRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("/new")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postPost(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Valid @RequestBody PostRequest postRequest) {
        Post post = new Post();
        if (postRequest.getTitle().isEmpty()){
            return ResponseEntity.badRequest().body("Title is empty");
        }

        if (postRequest.getContent().isEmpty()){
            return ResponseEntity.badRequest().body("Content is empty");
        }
        String username = jwtUtils.getUserNameFromJwtToken(authorization);
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            return new RuntimeException();
        });
        post.setUser(user);

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        Post saved = postRepo.save(post);
        PostResponse response = new PostResponse(saved.getId(),saved.getTitle(),saved.getContent(),saved.getCreatedAt());
        return ResponseEntity.ok().body(response);
    }
}
