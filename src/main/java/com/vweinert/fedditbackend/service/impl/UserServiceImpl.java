package com.vweinert.fedditbackend.service.impl;

import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.exception.ResourceNotFoundException;
import com.vweinert.fedditbackend.payload.request.LoginRequest;
import com.vweinert.fedditbackend.payload.response.JwtResponse;
import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
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
    public User registerUser(User user) throws Exception{
        return null;
    }
    public User sigInUser(LoginRequest request) throws Exception{
        return null;
    }
}
