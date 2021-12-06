package it.theboys.project0002api.service;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public String register() {
        return null;
    }

    @Override
    public String guestLogin() {
        return null;
    }

    @Override
    public String login() {
        return null;
    }

    @Override
    public String getUserList() {
        return null;
    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public Object getUserById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteUser(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public User modifyUser(String id, String request) {
        // TODO Auto-generated method stub
        return null;
    }
}