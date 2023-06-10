package com.epam.esm.controller;

import com.epam.esm.domain.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    UserService userService;

    /**
     * return user with {id}
     *
     * @param id
     * @return found user or 404 if user not exist
     */
    @GetMapping("/{id}")
    public User fetchById(@PathVariable long id) {
        return userService.getUser(id).orElseThrow(() -> new EntityNotFoundException("Requested resource not found (user id =" + id + ")", Error.UserNotFound));
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Return all users;
     *
     * @return List of User
     */
    @GetMapping()
    public List<User> featchAllUers() {
        return userService.getAllUsers();
    }
}
