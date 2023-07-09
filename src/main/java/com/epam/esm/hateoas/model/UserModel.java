package com.epam.esm.hateoas.model;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserModel extends RepresentationModel<User> {
    private long id;
    private String login;
    private Set<Order> orders;
    private Set<Role> roles = new HashSet<>();
}
