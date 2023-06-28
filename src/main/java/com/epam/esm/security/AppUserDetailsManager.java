package com.epam.esm.security;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserDetailsManager implements UserDetailsManager {
    @Autowired
    UserService userService;


    @Override
    public void createUser(UserDetails user) {
        userService.addUser(user);
    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {
        userService.deleteUser(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        userService.updateUserInfo(oldPassword, newPassword);
    }

    @Override
    public boolean userExists(String username) {
        return userService.userExist(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUser(username).get();
    }
}
