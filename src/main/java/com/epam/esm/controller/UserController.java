package com.epam.esm.controller;

import com.epam.esm.domain.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.hateoas.assembler.UserModelAssembler;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserModelAssembler modelAssembler;

    /**
     * return user with {id}
     *
     * @param id
     * @return found user or 404 if user not exist
     */
    @GetMapping("/{id}")
    public UserModel fetchById(@PathVariable long id) {
        return modelAssembler.toModel(userService.getUser(id).orElseThrow(() -> new EntityNotFoundException("Requested resource not found (user id =" + id + ")", Error.UserNotFound)));
    }

    /**
     * Return all users;
     *
     * @return List of User
     */
    @GetMapping()
    public List<User> fetchAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(params = {"login"})
    public UserModel fetchByLogin(@RequestParam(required = true) String login) {
        return modelAssembler.toModel(userService.getUser(login).orElseThrow(() -> new EntityNotFoundException("User not found", Error.UserNotFound)));
    }
}
