package com.vweinert.fedditbackend.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.vweinert.fedditbackend.models.Comment;
import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.payload.request.CommentRequest;
import com.vweinert.fedditbackend.payload.response.CommentResponse;
import com.vweinert.fedditbackend.repository.CommentRepository;
import com.vweinert.fedditbackend.repository.PostRepository;
import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    PostRepository postRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    CommentRepository commentRepo;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Valid @RequestBody CommentRequest request, @PathVariable String postId){
        Long id;
        try {
            id = Long.parseLong(postId);
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
            Comment saving = new Comment();
            saving.setUser(user);
            saving.setPost(post.get());
            saving.setContent(request.getContent());
            Comment saved = commentRepo.save(saving);
            CommentResponse response = new CommentResponse();
            response.setContent(saved.getContent());
            response.setId(saved.getId());
            response.setPostId(saved.getPost().getId());
            response.setUserId(saved.getUser().getId());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body("tried putting comment on non-existent post");
        }
    }

    @GetMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getComment(@PathVariable String commentId){
        Long id;
        try {
            id = Long.parseLong(commentId);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("cannot parse id");
        }
        Optional<Comment> comment = commentRepo.findById(id);
        if (comment.isPresent()){
            CommentResponse response = new CommentResponse();
            response.setContent(comment.get().getContent());
            response.setId(comment.get().getId());
            response.setPostId(comment.get().getPost().getId());
            response.setUserId(comment.get().getUser().getId());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body("tried getting non-existent comment");
        }
    }
}
