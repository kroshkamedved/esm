package com.epam.esm.service;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.MySqlUserRepositoryImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Setter
public class UserService {
    UserRepository userRepository;

    @Autowired
    public void setUserRepository(MySqlUserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(long id) {
        return Optional.ofNullable(userRepository.fetchUser(id));
    }

    public List<User> getAllUsers() {
        return userRepository.fetchAllUsers();
    }
}
