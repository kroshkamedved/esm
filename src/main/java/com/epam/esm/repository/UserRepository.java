package com.epam.esm.repository;

import com.epam.esm.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository {
    User fetchUser(long id);

    List<User> fetchAllUsers();

    User fetchUserWithHighestOrdersCost();

    void createUser(UserDetails user);
}
