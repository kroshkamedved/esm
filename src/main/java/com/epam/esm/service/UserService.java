package com.epam.esm.service;

import com.epam.esm.domain.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.springdata.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SpringDataUserRepository springDataUserRepository;

    public Optional<User> getUser(long id) {
        return springDataUserRepository.findById(id);
    }

    public Optional<User> getUser(String login) {
        return springDataUserRepository.findByLogin(login);
    }

    public List<User> getAllUsers() {
        return userRepository.fetchAllUsers();
    }

    public void addUser(UserDetails user) {
        User newUSer = (User) user;
        springDataUserRepository.save(newUSer);
    }

    public void updateUserInfo(String oldPassword, String newPassword) {
    }

    public void deleteUser(String username) {
        springDataUserRepository.deleteByLogin(username);
    }

    public boolean userExist(String username) {
        return springDataUserRepository.existsByLogin(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return springDataUserRepository.findByLogin(username).orElseThrow(() -> new EntityNotFoundException("user not found", Error.UserNotFound));
    }
}
