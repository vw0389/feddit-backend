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

import com.vweinert.fedditbackend.entities.Comment;
import com.vweinert.fedditbackend.entities.Post;
import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    JwtUtils jwtUtils;
    public CommentController( JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @PostMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String postId){
        return null;
    }

    @GetMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getComment(@PathVariable String commentId){
        return null;
    }
}
