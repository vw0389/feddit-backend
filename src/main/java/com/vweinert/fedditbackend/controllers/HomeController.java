package com.vweinert.fedditbackend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vweinert.fedditbackend.entities.Post;
import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.payload.response.PostResponse;
import com.vweinert.fedditbackend.repository.PostRepository;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    PostRepository postRepo;
    @GetMapping("/mostRecent")
    public ResponseEntity<?> getMostRecent() {
        
        Optional<Post> post = postRepo.findMostRecent();
        if (post.isPresent()) {
            PostResponse response = new PostResponse();
            User userResponse = new User();
            userResponse.setDeleted(null);
            userResponse.setId(post.get().getId());
            userResponse.setUsername(post.get().getUser().getUsername());
            response.setId(post.get().getId());
            response.setUser(userResponse);
            response.setContent(post.get().getContent());
            response.setTitle(post.get().getTitle());
            response.setComments(post.get().getComments());
            
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body("No posts are in the db");
        }
        
    }
    @GetMapping("/TenMostRecent")
    public ResponseEntity<?> getTenMostRecent() {
        
        List<Post> posts = postRepo.findTenMostRecent();
        if (!posts.isEmpty()) {
            List<PostResponse> postsInResposneFormat = new ArrayList<>();
            for(Post post: posts) {
                PostResponse response = new PostResponse();
                User userResponse = new User();
                userResponse.setDeleted(null);
                userResponse.setId(post.getUser().getId());
                userResponse.setUsername(post.getUser().getUsername());
                response.setId(post.getId());
                response.setUser(userResponse);
                response.setContent(post.getContent());
                response.setTitle(post.getTitle());
                response.setComments(post.getComments());
                postsInResposneFormat.add(response);
            }
            
            
            return ResponseEntity.ok().body(postsInResposneFormat);
        } else {
            return ResponseEntity.badRequest().body("No posts are in the db");
        }
        
    }
}
