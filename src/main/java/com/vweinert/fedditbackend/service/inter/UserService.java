package com.vweinert.fedditbackend.service.inter;
import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.payload.request.LoginRequest;
import com.vweinert.fedditbackend.payload.response.JwtResponse;

public interface UserService {
    boolean isUserDeleted(User user) throws Exception;
    User registerUser(User user) throws Exception;
    User sigInUser(LoginRequest request)throws Exception;
}
