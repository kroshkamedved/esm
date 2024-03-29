package com.epam.esm.controller;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.dto.JWTTokenDTO;
import com.epam.esm.dto.LoginRequestDTO;
import com.epam.esm.security.AppUserDetailsManager;
import com.epam.esm.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> login(@RequestBody LoginRequestDTO user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(
                        HttpMethod.POST,
                        HttpMethod.OPTIONS
                ).
                body(new JWTTokenDTO(jwtTokenUtil.generateJwtToken(authentication), user.username()));
    }

    @PostMapping("/signup")
    public String signup(@RequestBody LoginRequestDTO user, HttpServletResponse response) {
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
