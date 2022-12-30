package com.vweinert.fedditbackend.controllers;

import com.vweinert.fedditbackend.dto.CommentDto;
import com.vweinert.fedditbackend.request.comment.PostComment;
import com.vweinert.fedditbackend.request.comment.PutComment;
import com.vweinert.fedditbackend.service.inter.CommentService;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import com.vweinert.fedditbackend.entities.Comment;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final JwtUtils jwtUtils;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    public CommentController(JwtUtils jwtUtils, CommentService commentService, ModelMapper modelMapper) {
        this.jwtUtils = jwtUtils;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }
    @PostMapping("/{strPostId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Validated(PostComment.class) @RequestBody Comment commentRequest, @PathVariable String strPostId){
        long userId = jwtUtils.getUserIdFromJwtToken(authorization.substring(7));
        try {
            long postId = getId(strPostId);
            Comment comment = commentService.createComment(userId, postId, commentRequest);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{strCommentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCommentById(@PathVariable String strCommentId){
        try {
            long commentId = getId(strCommentId);
            Comment comment = commentService.getComment(commentId);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putCommentById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Validated(PutComment.class) @RequestBody Comment commentRequest) {
        long userId = jwtUtils.getUserIdFromJwtToken(authorization.substring(7));
        try {
            Comment comment = commentService.updateComment(userId,commentRequest);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{strCommentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteCommentById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String strCommentId) {
        long userId = jwtUtils.getUserIdFromJwtToken(authorization.substring(7));
        try {
            long commentId = getId(strCommentId);
            Comment comment = commentService.deleteComment(userId,commentId);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private long getId(String id) {
        return Long.parseLong(id);
    }
}
