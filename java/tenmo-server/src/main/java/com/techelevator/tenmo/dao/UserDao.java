package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserWeb;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    public List<UserWeb> getUsers();

    boolean create(String username, String password);

    public UserWeb getUser(int userId);
}
