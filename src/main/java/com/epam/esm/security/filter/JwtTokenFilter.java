package com.epam.esm.security.filter;

import com.epam.esm.service.UserService;
import com.epam.esm.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = resolveToken(request);
        if (jwtToken == null || !(jwtTokenUtil.isValid(jwtToken))) {
            filterChain.doFilter(request, response);
        } else {
            UserDetails userDetails = userService.loadUserByUsername(
                    jwtTokenUtil.getUsername(jwtToken));
            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails == null ? List.of() : userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }

    private static String resolveToken(HttpServletRequest request) {
        String bearerHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerHeader == null || !(bearerHeader.startsWith("Bearer"))) {
            return null;
        }
        return bearerHeader.split(" ")[1].trim();
    }
}
