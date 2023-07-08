package com.epam.esm.controller;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.dto.LoginRequest;
import com.epam.esm.security.AppUserDetailsManager;
import com.epam.esm.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public String login(@RequestBody LoginRequest user, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateJwtToken(authentication);

    }

    @PostMapping("signup")
    public String signup(@RequestBody LoginRequest user, HttpServletResponse response) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setLogin(user.username());
        newUser.setRoles(Set.of(Role.ROLE_USER));

        userDetailsManager.createUser(newUser);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateJwtToken(authentication);

    }

}
