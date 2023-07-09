package com.epam.esm.repository.springdata;

import com.epam.esm.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringDataUserRepository extends CrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u set u.password = ?1 where u.login = ?2")
    void updateUserInfo(String password, String login);

    void deleteByLogin(String username);

    boolean existsByLogin(String username);

    Optional<User> findByLogin(String login);
}
