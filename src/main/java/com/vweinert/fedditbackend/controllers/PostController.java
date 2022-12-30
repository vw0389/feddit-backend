package com.vweinert.fedditbackend.controllers;

import com.vweinert.fedditbackend.dto.PostDto;
import com.vweinert.fedditbackend.service.inter.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vweinert.fedditbackend.entities.Post;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    public PostController(PostService postService, ModelMapper modelMapper, JwtUtils jwtUtils) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.jwtUtils = jwtUtils;
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postPost(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Post postRequest) {
        long userId = jwtUtils.getUserIdFromJwtToken(authorization.substring(7));
        try {
            Post post = postService.createPost(userId, postRequest);
            PostDto postDto = modelMapper.map(post,PostDto.class);
            return ResponseEntity.ok().body(postDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/{strPostId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPostById(@PathVariable String strPostId) {
        try {
            long postId = getId(strPostId);
            Post post = postService.getPostById(postId);
            PostDto postDto = modelMapper.map(post,PostDto.class);
            return ResponseEntity.ok().body(postDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }
    @PutMapping("/{strPostId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putPostById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Post postRequest, @PathVariable String strPostId) {
        long userId = jwtUtils.getUserIdFromJwtToken(authorization.substring(7));
        try {
            long postId = getId(strPostId);
            Post post = postService.updatePost(userId, postId, postRequest);
            PostDto postDto = modelMapper.map(post,PostDto.class);
            return ResponseEntity.ok().body(postDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{strPostId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePostById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String strPostId) {

        long userId = jwtUtils.getUserIdFromJwtToken(authorization.substring(7));
        try {
            long postId = getId(strPostId);
            Post post = postService.deletePost(userId,postId);
            PostDto postDto = modelMapper.map(post,PostDto.class);
            return ResponseEntity.ok().body(postDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    private long getId(String id) {
        return Long.parseLong(id);
    }

}
