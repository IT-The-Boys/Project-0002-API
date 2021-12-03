package it.theboys.project0002api.service;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

public interface UserService {

    String register();
    String guestLogin();
    String login();
    String getUserList();
    String getUser();
    Object getUserById(String id);
    void deleteUser(String id);
    User modifyUser(String id, String request);
}