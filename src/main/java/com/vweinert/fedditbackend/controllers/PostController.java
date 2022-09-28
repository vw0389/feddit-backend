package com.vweinert.fedditbackend.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.vweinert.fedditbackend.models.Comment;
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
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postPost(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Valid @RequestBody PostRequest postRequest) {
        if (postRequest.getTitle().isEmpty()){
            return ResponseEntity.badRequest().body("Title is empty");
        }

        if (postRequest.getContent().isEmpty()){
            return ResponseEntity.badRequest().body("Content is empty");
        }
        String jwt = authorization.split(" ")[1];
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            return new RuntimeException();
        });
        if (user.getDeleted()){
            return ResponseEntity.badRequest().body("user is deleted");
        }
        Post post = new Post();
        post.setUser(user);

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        Post saved = postRepo.save(post);
        PostResponse response = new PostResponse(saved.getId(),saved.getTitle(),saved.getContent(),saved.getCreatedAt());
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{postid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPostById(@PathVariable String postid) {
        Long id;
        try {
            id = Long.parseLong(postid);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("cannot parse id");
        }
        
        Optional<Post> post = postRepo.findById(id);
        if(post.isPresent()){
            User userResponse = new User();
            userResponse.setDeleted(null);
            userResponse.setId(post.get().getId());
            userResponse.setUsername(post.get().getUser().getUsername());
            PostResponse response = new PostResponse();
            response.setUser(userResponse);
            response.setId(post.get().getId());
            response.setTitle(post.get().getTitle());
            response.setContent(post.get().getContent());
            
            response.setCreatedAt(post.get().getCreatedAt());
            response.setModifiedAt(post.get().getModifiedAt());
            response.setComments(post.get().getComments());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body("id does not correspond to a post");
        }
        
    }
    @PutMapping("/{postid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putPostById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Valid @RequestBody PostRequest postRequest, @PathVariable String postid) {
        Long id;
        try {
            id = Long.parseLong(postid);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("cannot parse id");
        }
        String jwt = authorization.split(" ")[1];
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            return new RuntimeException("user not found");
        });
        Optional<Post> post = postRepo.findById(id);

        if(post.isPresent()){
            if (user.equals(post.get().getUser())) {
                return ResponseEntity.badRequest().body("tried to put on another person's post");
            } else{
                post.get().setContent(postRequest.getContent());
                post.get().setModifiedAt(LocalDateTime.now());
                Post saved = postRepo.save(post.get());
                PostResponse response = new PostResponse();
                response.setId(saved.getId());
                response.setTitle(saved.getTitle());
                response.setContent(saved.getContent());
                response.setCreatedAt(saved.getCreatedAt());
                response.setModifiedAt(saved.getModifiedAt());
                response.setComments(saved.getComments());
                return ResponseEntity.ok().body(response);
            }
        } else {
            return ResponseEntity.badRequest().body("id does not correspond to a post");
        }
    
    }
    @DeleteMapping("/{postid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePostById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String postid) {
        Long id;
        try {
            id = Long.parseLong(postid);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("cannot parse id");
        }
        String jwt = authorization.split(" ")[1];
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            return new RuntimeException("user not found");
        });
        Optional<Post> post = postRepo.findById(id);
        if (post.isPresent()){
            if (!post.get().getUser().equals(user)) {
                return ResponseEntity.badRequest().body("tried to delete another person's post");
            } else {
                postRepo.deleteById(post.get().getId());
                return ResponseEntity.ok().body("post deleted");
            }
        } else {
            return ResponseEntity.badRequest().body("post does not exist");
        }
    }
    
    
}
