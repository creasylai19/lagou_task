package com.lagou.edu.service.impl;

import com.lagou.edu.pojo.User;
import com.lagou.edu.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {

    @Override
    public boolean login(User user) {
        if( null == user ) {
            return false;
        }
        String username = user.getUsername();
        String password = user.getPassword();
        if( "admin".equals(username) && "admin".equals(password) ) {
            return true;
        }
        return false;
    }

}
