package com.vweinert.fedditbackend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vweinert.fedditbackend.dto.PostDto;
import com.vweinert.fedditbackend.entities.Post;

import com.vweinert.fedditbackend.service.inter.PostService;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/home")
public class HomeController {
    String noPostsInDb = "no posts in db";
    @Autowired
    private PostService postService;
    @Autowired
	private ModelMapper modelMapper;
    @GetMapping("/mostRecent")
    public ResponseEntity<?> getMostRecent() {
        Optional<Post> post = postService.getMostRecentPost();
        if (post.isPresent()){
            PostDto postDto = modelMapper.map(post.get(), PostDto.class);
            return ResponseEntity.ok().body(postDto);
        } else {
            return ResponseEntity.badRequest().body(noPostsInDb);
        }
        
    }
    @GetMapping("/TenMostRecent")
    public ResponseEntity<?> getTenMostRecent() {
        List<Post> posts = postService.getTenMostRecentPosts();
        if (!posts.isEmpty()){
            List<PostDto> postsDtos = new ArrayList<>();
            for(Post post: posts) {
                postsDtos.add(modelMapper.map(post,PostDto.class));
            }
            return ResponseEntity.ok().body(postsDtos);
        } else {
            return ResponseEntity.badRequest().body(noPostsInDb);
        }
    }
}
