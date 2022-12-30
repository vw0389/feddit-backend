package com.vweinert.fedditbackend.service.impl;

import com.vweinert.fedditbackend.entities.ERole;
import com.vweinert.fedditbackend.entities.Role;
import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.exception.ResourceNotFoundException;
import com.vweinert.fedditbackend.repository.RoleRepository;
import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;
import com.vweinert.fedditbackend.service.inter.UserService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final Set<Role> userRoles;
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils,Set<Role> userRoles){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userRoles = new HashSet<>();
        if (!this.roleRepository.existsByName(ERole.ROLE_USER)) {
            this.roleRepository.save(new Role(ERole.ROLE_USER));
        }
        Optional<Role> userRole = this.roleRepository.findByName(ERole.ROLE_USER);

        this.userRoles.add(userRole.get());
    }
    public boolean isUserDeleted(User user) throws Exception {
        if(user.getDeleted()){
            return true;
        }
        Optional<User> userFromRepo = userRepository.findById(user.getId());
        if (userFromRepo.isPresent()) {
            if(userFromRepo.get().getDeleted()){
                return true;
            } else {
                return false;
            }
        } else {
            throw new ResourceNotFoundException("user not found");
        }
    }
    public User registerUser(User user) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("username already in use");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("email already in use");
        }

        User saving = User
                .builder()
                .about(user.getAbout())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(encoder.encode(user.getPassword()))
                .roles(userRoles)
                .build();
        User saved = userRepository.save(saving);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        saved.setJwt(jwt);
        return saved;
    }


    public User sigInUser(User user) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        Optional<User> optionalFromRepo = userRepository.findByUsername(user.getUsername());
        if (optionalFromRepo.isPresent()) {
            User fromRepo = optionalFromRepo.get();
            fromRepo.setJwt(jwt);
            return fromRepo;
        } else {
            throw new Exception("an error occured");
        }
    }
}
