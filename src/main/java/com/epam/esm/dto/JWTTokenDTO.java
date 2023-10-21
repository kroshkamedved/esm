package com.epam.esm.dto;

import lombok.Data;

@Data
public class JWTTokenDTO {
    private final String JWT;
    private final String user;

    public JWTTokenDTO(String jwt, String user) {
        JWT = jwt;
        this.user = user;
    }
}
