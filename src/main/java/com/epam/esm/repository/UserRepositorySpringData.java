package com.epam.esm.repository;

import com.epam.esm.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepositorySpringData extends CrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u set u.password = ?1 where u.login = ?2")
    void updateUserInfo(String password, String login);

    void deleteByLogin(String username);

    boolean existsByLogin(String username);

    Optional<User> findByLogin(String login);
}
