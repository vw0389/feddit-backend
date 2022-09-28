package com.vweinert.fedditbackend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.payload.response.PostResponse;
import com.vweinert.fedditbackend.repository.PostRepository;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    PostRepository postRepo;
    @GetMapping
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
}
