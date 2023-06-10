package com.epam.esm.repository.impl;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Setter
@Repository
public class MySqlUserRepositoryImpl implements UserRepository {
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    EntityManager entityManager;

    public User fetchUser(long id) {
        return entityManager.find(User.class, id);
    }

    public List<User> fetchAllUsers() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        CriteriaQuery<User> all = criteriaQuery.select(root);
        return entityManager.createQuery(all).getResultList();
    }
}
