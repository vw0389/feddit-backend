package com.vweinert.fedditbackend.service.inter;

import com.vweinert.fedditbackend.models.User;

public interface UserService {
    boolean isUserDeleted(User user) throws Exception;
    User registerUser(User user) throws Exception;
    User sigInUser(User user)throws Exception;
}
