package com.epam.esm.repository;

import com.epam.esm.domain.User;

import java.util.List;

public interface UserRepository {
    User fetchUser(long id);

    List<User> fetchAllUsers();

    User fetchUserWithHighestOrdersCost();
}
