package com.vweinert.fedditbackend.controllers;

import com.vweinert.fedditbackend.dto.AuthDto;
import com.vweinert.fedditbackend.dto.PostDto;
import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Rollback
public class PostControllerTest {
    @Autowired
    private AuthController authController;
    @Autowired
    private PostController postController;
    @Autowired
    private UserRepository userRepository;
    private static final String testEmail1 = "emaily@gmturtlesail.com";
    private static final String testUsername1=  "usernameturltes";
    private static final String testPassword1 = "password123";
    private static final String testEmail2 = "emaily2@gmturtlesail.com";
    private static final String testUsername2=  "usernameturltes2";
    private static final String testPassword2= "password123";

//    @Test
//    public void testUserSignupAndPost() {
//        User user = User.builder()
//                .email(testEmail1)
//                .username(testUsername1)
//                .password(testPassword1)
//                .build();
//
//        ResponseEntity<AuthDto> goodResponse =  (ResponseEntity<AuthDto>) authController.registerUser(user);
//
//        assertThat(goodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        AuthDto body = goodResponse.getBody();
//
//        assertThat(body).isNotNull();
//        assertThat(body.getUsername()).isEqualTo(testUsername1);
//        assertThat(body.getId()).isNotNull();
//        assertThat(body.getJwt()).isNotEmpty();
//        assertThat(body.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
//
//        String user1Jwt = "Bearer " + body.getJwt();
//        String title = "hello world";
//        String content = "this is some post's content";
//        Post post = Post.builder()
//                .title(title)
//                .content(content)
//                .build();
//
//        ResponseEntity<PostDto> postDtoResponseEntity = (ResponseEntity<PostDto>) postController.postPost(user1Jwt,post);
//
//        assertThat(postDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        PostDto postBody = postDtoResponseEntity.getBody();
//
//        assertThat(postBody).isNotNull();
//        assertThat(postBody.getTitle()).isEqualTo(title);
//        assertThat(postBody.getContent()).isEqualTo(content);
//        assertThat(postBody.getComments()).isNull();
//        assertThat(postBody.getCreatedAt()).isBefore(LocalDateTime.now());
//        assertThat(postBody.getUpdatedAt()).isBefore(LocalDateTime.now());
//        assertThat(postBody.isDeleted()).isEqualTo(false);
//
//        Optional<User> user1 = userRepository.findByUsername(testUsername1);
//        if (user1.isPresent()) {
//            assertThat(postBody.getUser().getId()).isEqualTo(user1.get().getId());
//            assertThat(postBody.getUser().getUsername()).isEqualTo(user1.get().getUsername());
//            assertThat(postBody.getUser().getPosts()).isNullOrEmpty();
//        } else {
//            assert false;
//        }
//
//
//    }
//
//    @Test
//    public void testPostUpdate() {
//        User user = User.builder()
//                .email(testEmail2)
//                .username(testUsername2)
//                .password(testPassword2)
//                .build();
//
//        ResponseEntity<AuthDto> goodResponse =  (ResponseEntity<AuthDto>) authController.registerUser(user);
//
//        assertThat(goodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        AuthDto body = goodResponse.getBody();
//
//        assertThat(body).isNotNull();
//        assertThat(body.getUsername()).isEqualTo(testUsername2);
//        assertThat(body.getId()).isNotNull();
//        assertThat(body.getJwt()).isNotEmpty();
//        assertThat(body.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
//
//        String user2Jwt = "Bearer " + body.getJwt();
//        String title = "hello world";
//        String content = "this is some post's content";
//        Post post = Post.builder()
//                .title(title)
//                .content(content)
//                .build();
//        ResponseEntity<PostDto> postDtoResponseEntity = (ResponseEntity<PostDto>) postController.postPost(user2Jwt,post);
//
//        assertThat(postDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        PostDto postBody = postDtoResponseEntity.getBody();
//
//        assertThat(postBody).isNotNull();
//        assertThat(postBody.getTitle()).isEqualTo(title);
//        assertThat(postBody.getContent()).isEqualTo(content);
//        assertThat(postBody.getComments()).isNull();
//        assertThat(postBody.getCreatedAt()).isBefore(LocalDateTime.now());
//        assertThat(postBody.getUpdatedAt()).isBefore(LocalDateTime.now());
//        assertThat(postBody.isDeleted()).isEqualTo(false);
//
//        Optional<User> user1 = userRepository.findByUsername(testUsername2);
//        if (user1.isPresent()) {
//            assertThat(postBody.getUser().getId()).isEqualTo(user1.get().getId());
//            assertThat(postBody.getUser().getUsername()).isEqualTo(user1.get().getUsername());
//            assertThat(postBody.getUser().getPosts()).isNullOrEmpty();
//        } else {
//            assert false;
//        }
//
//        String newContent = "This is some rambunctious turtles";
//        Post postUpdated = Post.builder()
//                .title(title)
//                .content(newContent)
//                .build();
//        String id = postBody.getId().toString();
//
//        ResponseEntity<PostDto> updatedDtoResponseEntity = (ResponseEntity<PostDto>) postController.putPostById(user2Jwt,postUpdated,id);
//
//        assertThat(updatedDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        PostDto updatedPostBody = postDtoResponseEntity.getBody();
//
//        assertThat(updatedPostBody.getCreatedAt()).isNotNull();
//
//        assertThat(updatedPostBody.getUpdatedAt()).isNotNull();
//        assertThat(updatedPostBody.getUpdatedAt()).isAfter(updatedPostBody.getCreatedAt());
//        assertThat(updatedPostBody.getUpdatedAt()).isBefore(LocalDateTime.now());
//        assertThat(updatedPostBody.isDeleted()).isFalse();
//        assertThat(updatedPostBody.getContent()).isEqualTo(newContent);
//        assertThat(updatedPostBody.getTitle()).isEqualTo(title);
//        assertThat(updatedPostBody.getId()).isEqualTo(postBody.getId());
//
//
//    }

}
